/*
 Copyright 2014 Red Hat, Inc. and/or its affiliates.

 This file is part of darcy-web.

 This program is free software: you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation, either version 3 of the License, or
 (at your option) any later version.

 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.redhat.darcy.web;

import static com.redhat.darcy.web.HtmlElements.htmlElement;

import com.redhat.darcy.ui.AbstractViewElement;
import com.redhat.darcy.ui.api.Locator;
import com.redhat.darcy.ui.api.elements.Table;
import com.redhat.darcy.web.api.WebContext;
import com.redhat.darcy.web.api.elements.HtmlElement;

import java.util.List;

/**
 * An extendable base {@link com.redhat.darcy.ui.api.ViewElement} describing simple, semantic HTML
 * tables, constructed of the {@code <table>}, {@code <tr>}, {@code <td>}, and (optionally)
 * {@code <tbody>} and {@code <thead>} tags.
 *
 * As outlined in the documentation for {@link com.redhat.darcy.ui.api.elements.Table}, extend this
 * table not specifically to add extensions to the class or implement any methods, but to define
 * columns: externally visible instances of {@link com.redhat.darcy.ui.api.elements.Table.Column}
 * that have visibility into the table represented by this ViewElement by means of
 * {@link #byRowColumn(int, int)}, {@link #byHeader(int)}, and
 * {@link #byInner(com.redhat.darcy.ui.api.Locator, com.redhat.darcy.ui.api.Locator...)} , and can
 * therefore locate specific cells.
 *
 * <p>Example usage:
 *
 * <pre><code>
 *     import static org.hamcrest.Matchers.equalTo;
 *
 *     public class Staff extends AbstractView {
 *         {@literal @}Require
 *         private StaffEmailTable staffEmails = new StaffEmailTable(By.id("staffEmails"));
 *
 *         public HtmlLink getEmailLinkByStaff(String fullName) {
 *             return staffEmails.getRowsWhere(StaffEmailTable.FULL_NAME, equalTo((fullName)))
 *                     .findFirst()
 *                     .orElseThrow(() -&gt; new AssertionError("No staff member found by name, " + fullName)
 *                     .getCell(StaffEmailTable.EMAIL);
 *         }
 *
 *         private static class StaffEmailTable extends HtmlTable{@code<StaffEmailTable>} {
 *             private static final Column{@code<StaffEmailTable, String>} FULL_NAME =
 *                     (t, r) -&gt; t.getContext().find().text(t.byRowColumn(r, 1).getText();
 *             private static final Column{@code<StaffEmailTable, HtmlLink>} EMAIL =
 *                     (t, r) -&gt; t.getContext().find().htmlLink(t.byRowColumn(r, 2);
 *
 *             private StaffEmailTable(By table) {
 *                 super(table);
 *             }
 *         }
 *     }
 * </code></pre>
 *
 * @param <T> The type that is extending this class.
 */
public abstract class HtmlTable<T extends Table<T>> extends AbstractViewElement<HtmlElement> implements Table<T>,
        HtmlElement {
    private final HtmlElement bodyTag = htmlElement(byInner(By.htmlTag("tbody")));
    private final HtmlElement headerTag = htmlElement(byInner(By.htmlTag("thead")));

    public HtmlTable(Locator parent) { super(HtmlElement.class, parent); }
    public HtmlTable(HtmlElement parent) { super(parent); }

    @Override
    public WebContext getContext() {
        return (WebContext) super.getContext();
    }

    @Override
    public boolean isLoaded() {
        return isDisplayed();
    }

    @Override
    public boolean isDisplayed() {
        return parent.isDisplayed();
    }

    @Override
    public boolean isPresent() {
        return parent.isPresent();
    }

    /**
     * This will count all of the {@code<tr>} tags within the {@code<tbody>} tag if one is present,
     * otherwise will count all of the {@code<tr>} tags within the table.
     *
     * <p>If you're modeling an unconventional html table, you're encouraged to override this
     * method.
     */
    @Override
    public int getRowCount() {
        String xpath = bodyTag.isPresent()
            ? "./tbody/tr"
            : "./tr";

        return getContext().find().elements(byInner(By.xpath(xpath))).size();
    }

    @Override
    public boolean isEmpty() {
        return getRowCount() == 0;
    }

    @Override
    public void click() {
        parent.click();
    }

    @Override
    public String getTagName() {
        return parent.getTagName();
    }

    @Override
    public String getCssValue(String property) {
        return parent.getCssValue(property);
    }

    @Override
    public List<String> getClasses() {
        return parent.getClasses();
    }

    @Override
    public String getAttribute(String attribute) {
        return parent.getAttribute(attribute);
    }

    /**
     * Conveniently allows column implementations to lookup headers without duplicating the effort
     * to come up with xpath for each column's header. Simply use this method with that column's
     * index.
     *
     * @return A locator that finds a header cell based on a column index. It does this by
     * constructing an xpath. If a {@code<thead>} tag is present, this will use,
     * "./thead/tr[1]/th[colIndex]". If no {@code<thead>} tag is present, "./tr[1]/th[colIndex]"
     * will be used.
     *
     * <p>If your modelling an unconventionally structured html table, you're encouraged to override
     * this method.
     *
     * @param colIndex index of the column to find a header cell, starting from the left at 1.
     */
    protected Locator byHeader(int colIndex) {
        if (colIndex < 1) {
            throw new IllegalArgumentException("Column index must be greater than 0.");
        }

        String xpath = headerTag.isPresent()
                ? "./thead/tr[1]/th[" + colIndex + "]"
                : "./tr[1]/th[" + colIndex + "]";

        return byInner(By.xpath(xpath));
    }

    /**
     * Conveniently allows column implementations to lookup cells inside a column without
     * duplicating the effort to come up with xpath for each column's cells. Simply use this method
     * with that column's index.
     *
     * @return A locator that finds a cell based on a row and column index. It does this by
     * constructing an xpath. If a {@code<tbody>} tag is present, this will use,
     * "./tbody/tr[rowIndex]/td[colIndex]". If no {@code<tbody>} tag is present,
     * "./tr[rowIndex]/td[colIndex]" will be used.
     *
     * <p>If your modelling an unconventionally structured html table, you're encouraged to override
     * this method.
     *
     * @param rowIndex Starting from the top, at 1.
     * @param colIndex Starting from the left, at 1.
     */
    protected Locator byRowColumn(int rowIndex, int colIndex) {
        if (rowIndex < 1) {
            throw new IllegalArgumentException("Row index must be greater than 0.");
        }

        if (colIndex < 1) {
            throw new IllegalArgumentException("Column index must be greater than 0.");
        }

        String xpath = bodyTag.isPresent()
                ? "./tbody/tr[" + rowIndex + "]/td[" + colIndex + "]"
                : "./tr[" + rowIndex + "]/td[" + colIndex + "]";

        return byInner(By.xpath(xpath));
    }
}

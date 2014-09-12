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
import com.redhat.darcy.ui.api.elements.Element;
import com.redhat.darcy.ui.api.elements.Table;
import com.redhat.darcy.ui.internal.ViewList;
import com.redhat.darcy.web.api.WebContext;
import com.redhat.darcy.web.api.elements.HtmlElement;
import com.redhat.darcy.web.api.elements.HtmlLink;

import java.util.List;
import java.util.Set;

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
 * {@link #byInner(com.redhat.darcy.ui.api.Locator...)}, and can therefore locate specific cells.
 *
 * <p>Example usage:
 *
 * <code><pre>
 *     import static org.hamcrest.Matchers.equalTo;
 *
 *     public class Staff extends AbstractView {
 *         {@literal @}Require
 *         private StaffEmailTable staffEmails = new StaffEmailTable(By.id("staffEmails"));
 *
 *         public HtmlLink getEmailLinkByStaff(String fullName) {
 *             return staffEmails.getRowsWhere(StaffEmailTable.FULL_NAME, equalTo((fullName)))
 *                     .findFirst()
 *                     .orElseThrow(() -> new AssertionError("No staff member found by name, " + fullName)
 *                     .getCell(StaffEmailTable.EMAIL);
 *         }
 *
 *         private static class StaffEmailTable extends HtmlTable{@code<StaffEmailTable>} {
 *             private static final Column{@code<StaffEmailTable, String>} FULL_NAME =
 *                     (t, r) -> t.getContext().find().text(t.byRowColumn(r, 1).getText();
 *             private static final Column{@code<StaffEmailTable, HtmlLink>} EMAIL =
 *                     (t, r) -> t.getContext().find().htmlLink(t.byRowColumn(r, 2);
 *
 *             private StaffEmailTable(By table) {
 *                 super(table);
 *             }
 *         }
 *     }
 * </pre></code>
 */
public abstract class HtmlTable<T extends Table<T>> extends AbstractViewElement implements Table<T>, HtmlElement {
    private final HtmlElement bodyTag = htmlElement(byInner(By.htmlTag("tbody")));
    private final HtmlElement headerTag = htmlElement(byInner(By.htmlTag("thead")));

    public HtmlTable(Locator parent) { super(parent); }
    public HtmlTable(Element parent) { super(parent); }

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
        ((HtmlElement) parent).click();
    }

    @Override
    public String getTagName() {
        return ((HtmlElement) parent).getTagName();
    }

    @Override
    public String getCssValue(String property) {
        return ((HtmlElement) parent).getCssValue(property);
    }

    @Override
    public Set<String> getClasses() {
        return ((HtmlElement) parent).getClasses();
    }

    @Override
    public String getAttribute(String attribute) {
        return ((HtmlElement) parent).getAttribute(attribute);
    }

    protected Locator byHeader(int col) {
        if (col < 1) {
            throw new IllegalArgumentException("Column index must be greater than 0.");
        }

        String xpath = headerTag.isPresent()
                ? "./thead/tr[1]/th[" + col + "]"
                : "./tr[1]/th[" + col + "]";

        return byInner(By.xpath(xpath));
    }

    protected Locator byRowColumn(int row, int col) {
        if (row < 1) {
            throw new IllegalArgumentException("Row index must be greater than 0.");
        }

        if (col < 1) {
            throw new IllegalArgumentException("Column index must be greater than 0.");
        }

        String xpath = bodyTag.isPresent()
                ? "./tbody/tr[" + row + "]/td[" + col + "]"
                : "./tr[" + row + "]/td[" + col + "]";

        return byInner(By.xpath(xpath));
    }
}

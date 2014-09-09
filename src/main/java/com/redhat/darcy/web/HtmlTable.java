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
 * A reusable {@link com.redhat.darcy.ui.api.ViewElement} describing simple, semantic HTML tables,
 * constructed of the {@code <table>}, {@code <tr>}, {@code <td>}, and (optionally) {@code <tbody>}
 * tags. Rows and headers can be found within the table with special locators that have visibility
 * into the HtmlTable class, {@link #byRowColumn(HtmlTable, int, int)} and
 * {@link #byHeader(HtmlTable, int)}.
 *
 * <p>Define these columns as private static final constants in your page object that contains the
 * HtmlTable, or, if your specific table and column configuration is reused in multiple pages, in
 * your own subclass of HtmlTable with public or package visibility.
 *
 * <p>To use an HtmlTable as a field, see {@link #htmlTable(com.redhat.darcy.ui.api.Locator)} and
 * {@link #htmlTables(com.redhat.darcy.ui.api.Locator)}.
 *
 * <p>Example usage:
 *
 * <code><pre>
 *     import static com.redhat.darcy.web.HtmlTable.htmlTable;
 *     import static org.hamcrest.Matchers.equalTo;
 *
 *     public class Staff extends AbstractView {
 *         {@literal @}Require
 *         private Table{@code<HtmlTable>} staffEmails = htmlTable(By.id("staffEmails"));
 *
 *         private static final HtmlTable.Column{@code<String>} FULL_NAME = HtmlTable.Column.text(1);
 *         private static final HtmlTable.Column{@code<HtmlLink>} EMAIL = HtmlTable.Column.link(2);
 *
 *         public HtmlLink getEmailLinkByStaff(String fullName) {
 *             return staffEmails.getRowsWhere(FULL_NAME, equalTo((fullName)))
 *                     .findFirst()
 *                     .orElseThrow(() -> new AssertionError("No staff member found by name, " + fullName)
 *                     .getCell(EMAIL);
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

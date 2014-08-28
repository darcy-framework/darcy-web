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
import com.redhat.darcy.web.api.elements.HtmlText;

import java.util.List;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * A reusable {@link com.redhat.darcy.ui.api.ViewElement} describing simple, semantic HTML tables,
 * constructed of the {@code <table>}, {@code <tr>}, {@code <td>}, and (optionally) {@code <tbody>}
 * tags. Columns can defined instantiating new types of
 * {@link com.redhat.darcy.web.HtmlTable.HtmlColumn HtmlColumns}, or instantiating one of the
 * predefined types of common column configurations like basic text content via
 * {@link com.redhat.darcy.web.HtmlTable.HtmlStringColumn}, or links via
 * {@link com.redhat.darcy.web.HtmlTable.HtmlLinkColumn}.
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
 *         private static final HtmlColumn{@code<String>} FULL_NAME = new HtmlTextColumn(1);
 *         private static final HtmlColumn{@code<HtmlLink>} EMAIL = new HtmlLinkColumn(2);
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
public class HtmlTable extends AbstractViewElement implements Table<HtmlTable>, HtmlElement {
    private final HtmlElement bodyTag = htmlElement(byInner(By.htmlTag("tbody")));
    private final HtmlElement headerTag = htmlElement(byInner(By.htmlTag("thead")));

    public HtmlTable(Locator parent) { super(parent); }
    public HtmlTable(Element parent) { super(parent); }

    public static HtmlTable htmlTable(Locator parent) {
        return new HtmlTable(parent);
    }

    public static List<HtmlTable> htmlTables(Locator parents) {
        return new ViewList<>(HtmlTable::new, parents);
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

    // TODO: rowspan? colspan?
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

    /**
     * A partial {@link com.redhat.darcy.ui.api.elements.Table.ColumnDefinition} implementation for
     * {@link com.redhat.darcy.web.HtmlTable HtmlTables}.
     * @param <T> The type of contents within this column.
     */
    public static class HtmlColumn<T, U> implements ColumnWithHeaderDefinition<HtmlTable, T, U> {
        private final BiFunction<WebContext, Locator, T> rowDef;
        private final BiFunction<WebContext, Locator, U> headerDef;
        private final int index;

        /**
         * Creates a new column definition for a column within an
         * {@link com.redhat.darcy.web.HtmlTable}.
         *
         * @param cellDefinition A function that takes the context of the table, and a locator to
         * an element within that column. Together, they can be used to find a cell within that
         * column, or possibly an element nested within that cell. The function is expected to
         * return the content of that cell, typed appropriately by T.
         * @param index The index of the column, counting from 1, where 1 is the leftmost column.
         */
        public HtmlColumn(int index, BiFunction<WebContext, Locator, T> rowDef,
                BiFunction<WebContext, Locator, U> headerDef) {
            this.rowDef = rowDef;
            this.headerDef = headerDef;
            this.index = index;
        }

        @Override
        public U getHeader(HtmlTable table) {
            return headerDef.apply((WebContext) table.getContext(), table.byHeader(index));
        }

        @Override
        public T getCell(HtmlTable table, int row) {
            return rowDef.apply((WebContext) table.getContext(), table.byRowColumn(row, index));
        }
    }

    public static class HtmlStringColumn extends HtmlColumn<String, HtmlText> {
        /**
         * @param index The index of the column, counting from 1, where 1 is the leftmost column.
         */
        public HtmlStringColumn(int index) {
            super(index, (c, l) -> c.find().text(l).getText(), (c, l) -> c.find().htmlText(l));
        }
    }

    public static class HtmlLinkColumn extends HtmlColumn<HtmlLink, HtmlText> {
        /**
         * @param index The index of the column, counting from 1, where 1 is the leftmost column.
         */
        public HtmlLinkColumn(int index) {
            super(index, (c, l) -> c.find().htmlLink(By.chained(l, By.xpath("./a"))),
                    (c, l) -> c.find().htmlText(l));
        }
    }
}

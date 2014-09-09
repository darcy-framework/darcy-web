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

import static com.redhat.darcy.ui.Elements.label;
import static com.redhat.darcy.web.HtmlTable.htmlTable;
import static com.redhat.synq.Synq.after;
import static org.hamcrest.Matchers.equalTo;

import com.redhat.darcy.ui.AbstractViewElement;
import com.redhat.darcy.ui.DarcyException;
import com.redhat.darcy.ui.annotations.Context;
import com.redhat.darcy.ui.annotations.Require;
import com.redhat.darcy.ui.api.Locator;
import com.redhat.darcy.ui.api.elements.Element;
import com.redhat.darcy.ui.api.elements.Label;
import com.redhat.darcy.ui.api.elements.PaginatedSortableTable;
import com.redhat.darcy.ui.matchers.ViewMatchers;
import com.redhat.darcy.web.api.WebContext;
import com.redhat.darcy.web.api.elements.HtmlLink;

import java.time.temporal.ChronoUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JQueryDataTable extends AbstractViewElement implements
        PaginatedSortableTable<JQueryDataTable> {
    private static final Pattern SHOW_START = Pattern.compile(".*?([\\d,]+).*?[\\d,]+");
    private static final Pattern SHOW_END = Pattern.compile(".*?[\\d,]+.*?([\\d,]+)");
    private static final Pattern SHOW_TOTAL = Pattern.compile(".*?[\\d,]+.*?[\\d,]+ of ([\\d,]+)");

    @Require
    private HtmlTable table = htmlTable(byInner(By.htmlTag("table")));
    private Label info = label(byInner(By.css(".dataTables_info")));

    private HtmlLink navPrevious;
    private HtmlLink navNext;
    private HtmlLink navFirst;

    private Label isEmpty = label(byInner(By.css(".dataTables_empty")));

    @Context
    private WebContext context;

    private String tableId;

    /**
     * @param parent Locator for the wrapper div around the table element.
     */
    // TODO: It would be nice if clients could just locate their table and not the wrapper div
    // that jquery datatable plugin generates.
    public JQueryDataTable(Locator parent) {
        super(parent);
    }

    /**
     * @param parent The wrapper div around the table element.
     */
    public JQueryDataTable(Element parent) {
        super(parent);
    }

    @Override
    public int getRowCount() {
        if (isEmpty()) {
            return 0;
        }

        return getShowingEnd() - getShowingStart() + 1;
    }

    @Override
    public boolean isEmpty() {
        return isEmpty.isDisplayed();
    }

    @Override
    public JQueryDataTable toPage(int page) {
        if (page == 1 && navFirst().isDisplayed()) {
            return after(navFirst()::click)
                    .expectCallTo(this::getCurrentPage, equalTo(1))
                    .describedAs("the datatable to be at the first page")
                    .andThenExpect(this, ViewMatchers.isLoaded())
                    .waitUpTo(2, ChronoUnit.MINUTES); // TODO make configurable
        }

        while (page > getCurrentPage()) {
            navNext();
        }

        while (page < getCurrentPage()) {
            navPrevious();
        }

        return this;
    }

    @Override
    public JQueryDataTable previousPage() {
        if (!hasPreviousPage()) {
            throw new IndexOutOfBoundsException("There is no previous page to navigate to. Current "
                    + "page is " + getCurrentPage());
        }

        return after(navPrevious()::click)
                .expectCallTo(this::getCurrentPage, equalTo(getCurrentPage() - 1))
                .describedAs("the datatable's current page to be decremented by 1")
                .andThenExpect(this, ViewMatchers.isLoaded())
                .waitUpTo(2, ChronoUnit.MINUTES); // TODO make configurable
    }

    @Override
    public JQueryDataTable nextPage() {
        if (!hasNextPage()) {
            throw new IndexOutOfBoundsException("There is no next page to navigate to. Current page"
                    + " is " + getCurrentPage());
        }

        return after(navNext()::click)
                .expectCallTo(this::getCurrentPage, equalTo(getCurrentPage() + 1))
                .describedAs("the datatable's current page to be incremented by 1")
                .andThenExpect(this, ViewMatchers.isLoaded())
                .waitUpTo(2, ChronoUnit.MINUTES); // TODO make configurable
    }

    @Override
    public boolean hasNextPage() {
        return navNext().getClasses().contains("ui-state-disabled");
    }

    @Override
    public boolean hasPreviousPage() {
        return navPrevious().getClasses().contains("ui-state-disabled");
    }

    @Override
    public int getCurrentPage() {
        if (isEmpty()) {
            return 1;
        }

        return getShowingEnd() / getRowCount();
    }

    @Override
    public int getTotalEntries() {
        Matcher matcher = SHOW_TOTAL.matcher(info.getText());

        if (!matcher.find()) {
            throw new DarcyException("Could not determine the total number of entries displayable "
                    + "in JQuery DataTable. Info was, " + info.getText());
        }

        return Integer.parseInt(matcher.group(1).replaceAll(",", ""));
    }

    public int getShowingStart() {
        Matcher matcher = SHOW_START.matcher(info.getText());

        if (!matcher.find()) {
            throw new DarcyException("Could not determine index of first entry currently visible in"
                    + "JQuery DataTable. Info was, " + info.getText());
        }

        return Integer.parseInt(matcher.group(1).replaceAll(",", ""));
    }

    public int getShowingEnd() {
        Matcher matcher = SHOW_END.matcher(info.getText());

        if (!matcher.find()) {
            throw new DarcyException("Could not determine index of last entry currently visible in "
                    + "JQuery DataTable. Info was, " + info.getText());
        }

        return Integer.parseInt(matcher.group(1).replaceAll(",", ""));
    }

    /**
     * Many elements within the wrapper div have ids in the format, ${table's_id}_${suffix}. This
     * itself is a locator strategy, implemented by this method. The returned locator can be used
     * by itself to find one of these elements. It does not need to be nested inside of
     * {@link #byInner(com.redhat.darcy.ui.api.Locator...)} (it is already).
     *
     * @param suffix The suffix to append after the inner table's id, plus an _, in the format,
     * ${table's_id}_${suffix}.
     */
    protected Locator byIdSuffix(String suffix) {
        if (tableId == null) {
            tableId = table.getAttribute("id");
        }

        return byInner(By.id(tableId + "_" + suffix));
    }

    protected HtmlLink navNext() {
        if (navNext == null) {
            navNext = context.find().htmlLink(byIdSuffix("next"));
        }

        return navNext;
    }

    protected HtmlLink navPrevious() {
        if (navPrevious == null) {
            navPrevious = context.find().htmlLink(byIdSuffix("previous"));
        }

        return navPrevious;
    }

    protected HtmlLink navFirst() {
        if (navFirst == null) {
            navFirst = context.find().htmlLink(byIdSuffix("first"));
        }

        return navFirst;
    }
}

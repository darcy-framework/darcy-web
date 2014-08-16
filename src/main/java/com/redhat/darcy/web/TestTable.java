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

import com.redhat.darcy.ui.AbstractViewElement;
import com.redhat.darcy.ui.annotations.Context;
import com.redhat.darcy.ui.api.Locator;
import com.redhat.darcy.ui.api.elements.Element;
import com.redhat.darcy.ui.api.elements.Table;
import com.redhat.darcy.ui.api.elements.TableColumn;
import com.redhat.darcy.ui.api.elements.TableRow;
import com.redhat.darcy.web.api.Browser;

public class TestTable extends AbstractViewElement {
    public TestTable(Locator parent) { super(parent); }
    public TestTable(Element parent) { super(parent); }

    @Context
    private Browser browser;

    public class ColumnA implements TableColumn<String> {

        @Override
        public String getCell(int row) {
            return getContext().find().label(byInner(cell(row, 0))).readText();
        }
    }

    public class TestRow implements TableRow {
        private final int index;

        public TestRow(int index) {
            this.index = index;
        }

        @Override
        public int getIndex() {
            return index;
        }
    }

    private Locator cell(int row, int col) {
        return By.xpath("./tbody/tr["+row+"]/td["+col+"]");
    }
}

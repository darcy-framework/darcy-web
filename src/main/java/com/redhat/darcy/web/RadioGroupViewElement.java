/*
 Copyright 2014 Red Hat, Inc. and/or its affiliates.

 This file is part of darcy.

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
import com.redhat.darcy.ui.api.Locator;
import com.redhat.darcy.ui.api.elements.Element;
import com.redhat.darcy.ui.api.elements.Radio;
import com.redhat.darcy.ui.api.elements.RadioGroup;
import com.redhat.darcy.ui.internal.ViewList;

import java.util.List;

public class RadioGroupViewElement extends AbstractViewElement implements RadioGroup {
    public RadioGroupViewElement(Locator parent) {
        super(parent);
    }

    public RadioGroupViewElement(Element parent) {
        super(parent);
    }

    public static RadioGroupViewElement radioGroupElement(Locator parent) {
        return new RadioGroupViewElement(parent);
    }

    public static List<RadioGroupViewElement> radioGroupElements(Locator parents) {
        return new ViewList<>(RadioGroupViewElement::new, parents);
    }

    @Override
    public List<Radio> getOptions() {
        return By.nested(this, By.xpath("//input[@type='radio']")).findAll(Radio.class, getContext());
    }

    @Override
    public Radio getCurrentlySelectedOption() {
        for (Radio radio : getOptions()) {
            if (radio.isSelected()) {
                return radio;
            }
        }

        return null;
    }
}

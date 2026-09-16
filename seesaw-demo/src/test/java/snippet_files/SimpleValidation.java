/*
 * Portions created by Ernie Rael are
 * Copyright (C) 2026 Ernie Rael.  All Rights Reserved.
 *
 * The contents of this file are subject to the Mozilla Public
 * License Version 1.1 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a copy of
 * the License at http://www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS
 * IS" basis, WITHOUT WARRANTY OF ANY KIND, either express or
 * implied. See the License for the specific language governing
 * rights and limitations under the License.
 *
 * Contributor(s): Ernie Rael <errael@raelity.com>
 */

package snippet_files;

import java.awt.Container;
import java.util.function.Function;

import javax.swing.JFrame;
import javax.swing.JPanel;

// @start region=validation_example_import
import org.netbeans.validation.api.ui.ValidationGroup;
import org.netbeans.validation.api.ui.ValidationItem;
import org.netbeans.validation.api.ui.swing.SwingValidationGroup;
import org.netbeans.validation.api.ui.swing.ValidationPanel;

import dev.visdb.seesaw.contrib.simplevalidation.SVUtils;
// @end region=validation_example_import
import dev.visdb.seesaw.SsTextField;

public class SimpleValidation {
  // @start region=validation_example
  JFrame createFrame() {
    SsTextField txtSupplierName = new SsTextField();
    SsTextField txtSupplierCity = new SsTextField();

    Function<String, Boolean> validateSupplierName = (str) -> {
      return str == null || !str.matches("(?i).*oops.{0,2}$");
    };
    SwingValidationGroup.setComponentName(txtSupplierName, "Supplier Name");
    ValidationItem decoSupplierName = SVUtils.setDecoratorValidator(txtSupplierName,
        SVUtils.getStringValidator(validateSupplierName, () -> "Supplier name can not end with 'oops..'"));

    Function<String, Boolean> validateSupplierCity = (str) -> {
      return str == null || !str.matches(".*X");
    };
    SwingValidationGroup.setComponentName(txtSupplierCity, "Supplier City");
    ValidationItem decoSupplierCity = SVUtils.setDecoratorValidator(txtSupplierCity,
        SVUtils.getStringValidator(validateSupplierCity, () -> "City can not end in 'X'"));

    // Put our components in a JPanel as usual
    final Container uiPanel = new JPanel();
    uiPanel.add(txtSupplierName);
    uiPanel.add(txtSupplierCity);
    
    // Wrap the uiPanel in a ValidationPanel.
    ValidationPanel validationPanel = new ValidationPanel();
    validationPanel.setInnerComponent(uiPanel);
    // Add the ValidationItems to the validationPanel's ValidationGroup.
    ValidationGroup group = validationPanel.getValidationGroup();
    group.addItem(decoSupplierName, false);
    group.addItem(decoSupplierCity, false);
    
    JFrame frame = new JFrame();
    // Put the ValidiationPanel in the frame's contentPane.
    frame.add(validationPanel);
    frame.setVisible(true);
    frame.pack();
    
    return frame;
  }
  // @end region=validation_example
}
// vi: sw=2 ts=8

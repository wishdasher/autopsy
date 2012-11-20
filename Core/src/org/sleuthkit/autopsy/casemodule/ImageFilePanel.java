/*
 * Autopsy Forensic Browser
 *
 * Copyright 2012 Basis Technology Corp.
 * Contact: carrier <at> sleuthkit <dot> org
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.sleuthkit.autopsy.casemodule;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/**
 * ImageTypePanel for adding an image file such as .img, .E0x, .00x, etc.
 */
public class ImageFilePanel extends ImageTypePanel implements DocumentListener {
    private static ImageFilePanel instance;
    private PropertyChangeSupport pcs = new PropertyChangeSupport(this);
    private JFileChooser fc = new JFileChooser();

    /**
     * Creates new form ImageFilePanel
     */
    public ImageFilePanel() {
        initComponents();
        fc.setDragEnabled(false);
        fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fc.setMultiSelectionEnabled(false);
        fc.addChoosableFileFilter(AddImageVisualPanel1.allFilter);
        fc.addChoosableFileFilter(AddImageVisualPanel1.rawFilter);
        fc.addChoosableFileFilter(AddImageVisualPanel1.encaseFilter);
        fc.setFileFilter(AddImageVisualPanel1.allFilter);
        pathTextField.getDocument().addDocumentListener(this);
    }
    
    /**
     * Returns the default instance of a ImageFilePanel.
     */
    public static ImageFilePanel getDefault() {
        if (instance == null) {
            instance = new ImageFilePanel();
        }
        return instance;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pathLabel = new javax.swing.JLabel();
        browseButton = new javax.swing.JButton();
        pathTextField = new javax.swing.JTextField();

        setMinimumSize(new java.awt.Dimension(0, 65));
        setPreferredSize(new java.awt.Dimension(403, 65));

        org.openide.awt.Mnemonics.setLocalizedText(pathLabel, org.openide.util.NbBundle.getMessage(ImageFilePanel.class, "ImageFilePanel.pathLabel.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(browseButton, org.openide.util.NbBundle.getMessage(ImageFilePanel.class, "ImageFilePanel.browseButton.text")); // NOI18N
        browseButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                browseButtonActionPerformed(evt);
            }
        });

        pathTextField.setText(org.openide.util.NbBundle.getMessage(ImageFilePanel.class, "ImageFilePanel.pathTextField.text")); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(pathTextField)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(browseButton)
                .addGap(2, 2, 2))
            .addGroup(layout.createSequentialGroup()
                .addComponent(pathLabel)
                .addGap(0, 284, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(pathLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(browseButton)
                    .addComponent(pathTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void browseButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_browseButtonActionPerformed
        String oldText = pathTextField.getText();
        // set the current directory of the FileChooser if the ImagePath Field is valid
        File currentDir = new File(oldText);
        if (currentDir.exists()) {
            fc.setCurrentDirectory(currentDir);
        }

        int retval = fc.showOpenDialog(this);
        if (retval == JFileChooser.APPROVE_OPTION) {
            String path = fc.getSelectedFile().getPath();
            pathTextField.setText(path);
        }
        pcs.firePropertyChange(AddImageVisualPanel1.EVENT.FOCUS_NEXT.toString(), false, true);
    }//GEN-LAST:event_browseButtonActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton browseButton;
    private javax.swing.JLabel pathLabel;
    private javax.swing.JTextField pathTextField;
    // End of variables declaration//GEN-END:variables
        
    /**
     * Get the path of the user selected image.
     * @return the image path
     */
    @Override
    public String getImagePath() {
        return pathTextField.getText();
    }

    /**
     * Set the path of the image file.
     */
    @Override
    public void setImagePath(String s) {
        pathTextField.setText(s);
    }

    /**
     * Should we enable the next button of the wizard?
     * @return true if a proper image has been selected, false otherwise
     */
    @Override
    public boolean enableNext() {
        String path = getImagePath();
        boolean isExist = Case.pathExists(path);
        boolean isPhysicalDrive = Case.isPhysicalDrive(path);
        boolean isPartition = Case.isPartition(path);
        
        return (isExist || isPhysicalDrive || isPartition);
    }

    /**
     * Update functions are called by the pathTextField which has this set
     * as it's DocumentEventListener. Each update function fires a property change
     * to be caught by the parent panel.
     * @param e the event, which is ignored
     */
    @Override
    public void insertUpdate(DocumentEvent e) {
        pcs.firePropertyChange(AddImageVisualPanel1.EVENT.UPDATE_UI.toString(), false, true);
    }

    @Override
    public void removeUpdate(DocumentEvent e) {
        pcs.firePropertyChange(AddImageVisualPanel1.EVENT.UPDATE_UI.toString(), false, true);
    }

    @Override
    public void changedUpdate(DocumentEvent e) {
        pcs.firePropertyChange(AddImageVisualPanel1.EVENT.UPDATE_UI.toString(), false, true);
    }
    
    /**
     * Set the focus to the pathTextField.
     */
    @Override
    public void select() {
        pathTextField.requestFocusInWindow();
    }
    
    /**
     * @return the string form of this panel
     */
    @Override
    public String toString() {
        return "Image File";
    }
    
    @Override
    public void addPropertyChangeListener(PropertyChangeListener pcl) {
        pcs = new PropertyChangeSupport(this);
        pcs.addPropertyChangeListener(pcl);
    }
    
    @Override
    public void removePropertyChangeListener(PropertyChangeListener pcl) {
        pcs.removePropertyChangeListener(pcl);
    }

}
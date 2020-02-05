
/*
Code revised from Desktop Java Live:
http://www.sourcebeat.com/downloads/
*/

import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.validation.ValidationResult;
import com.jgoodies.validation.util.DefaultValidationResultModel;
import com.jgoodies.validation.util.ValidationResultModel;
import com.jgoodies.validation.util.ValidationUtils;
import com.jgoodies.validation.view.ValidationResultViewFactory;

public class ValidationHowExample extends JPanel {
    private JTextField nameField;
    private JTextField siteField;
    private JTextField feedField;
    private Feed feed;
    private ValidationResultModel validationResultModel;
    private ShowFeedInformationAction showFeedInformationAction;

    public ValidationHowExample() {
        DefaultFormBuilder formBuilder = new DefaultFormBuilder(new FormLayout(""));
        formBuilder.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        formBuilder.appendColumn("right:pref");
        formBuilder.appendColumn("3dlu");
        formBuilder.appendColumn("fill:p:g");

        this.nameField = new JTextField();
        formBuilder.append("Name:", this.nameField);
        this.siteField = new JTextField();
        formBuilder.append("Site Url:", this.siteField);
        this.feedField = new JTextField();
        formBuilder.append("Feed Url:", this.feedField);

        this.validationResultModel = new DefaultValidationResultModel();
        this.validationResultModel.addPropertyChangeListener(new ValidationListener());

        JComponent validationResultsComponent = ValidationResultViewFactory.createReportList(validationResultModel);
        formBuilder.appendRow("top:30dlu:g");

        CellConstraints cc = new CellConstraints();
        formBuilder.add(validationResultsComponent, cc.xywh(1, formBuilder.getRow() + 1, 3, 1, "fill, fill"));
        formBuilder.nextLine(2);
        formBuilder.append(new JButton(new ValidateAction()), 3);

        this.showFeedInformationAction = new ShowFeedInformationAction();
        formBuilder.append(new JButton(this.showFeedInformationAction), 3);

        createFeed();
        initializeFormElements();

        add(formBuilder.getPanel());
    }

    private ValidationResult validateIt() {
        ValidationResult validationResult = new ValidationResult();

        if (ValidationUtils.isEmpty(this.nameField.getText())) {
            validationResult.addError("The name field can not be blank.");
        } else if (!ValidationUtils.hasBoundedLength(this.nameField.getText(), 5, 14)) {
            validationResult.addError("The name field must be between 5 and 14 characters.");
        }

        if (!"ClientJava.com".equals(this.nameField.getText())) {
            validationResult.addWarning("This form prefers the feed ClientJava.com");
        }


        if (ValidationUtils.isEmpty(this.feedField.getText())) {
            validationResult.addError("The feed field can not be blank.");
        }

        if (ValidationUtils.isEmpty(this.siteField.getText())) {
            validationResult.addError("The site field can not be blank.");
        }

        return validationResult;
    }

    private class ValidationListener implements PropertyChangeListener {
        public void propertyChange(PropertyChangeEvent evt) {
            if (evt.getPropertyName() == ValidationResultModel.PROPERTYNAME_RESULT) {
                JOptionPane.showMessageDialog(null, "Validation has been performed");
                showFeedInformationAction.setEnabled(!validationResultModel.hasErrors());
            } else if (evt.getPropertyName() == ValidationResultModel.PROPERTYNAME_MESSAGES) {
                if (Boolean.TRUE.equals(evt.getNewValue())) {
                    JOptionPane.showMessageDialog(null, "Messages have been found.");
                }
            }
        }
    }

    private Feed getFeed() {
        return this.feed;
    }

    private void createFeed() {
        this.feed = new Feed();
        this.feed.setName("ClientJava.com");
        this.feed.setSiteUrl("http://www.clientjava.com/blog");
        this.feed.setFeedUrl("http://www.clientjava.com/blog/rss.xml");
    }

    private void initializeFormElements() {
        this.nameField.setText(this.feed.getName());
        this.siteField.setText(this.feed.getSiteUrl());
        this.feedField.setText(this.feed.getFeedUrl());
    }

    private void synchFormAndFeed() {
        this.feed.setName(this.nameField.getText());
        this.feed.setSiteUrl(this.siteField.getText());
        this.feed.setFeedUrl(this.feedField.getText());
    }

    private class ValidateAction extends AbstractAction {
        public ValidateAction() {
            super("Validate");
        }

        public void actionPerformed(ActionEvent e) {
            synchFormAndFeed();
            ValidationResult validationResult = validateIt();
            validationResultModel.setResult(validationResult);
        }
    }

    private class ShowFeedInformationAction extends AbstractAction {
        public ShowFeedInformationAction() {
            super("Show Feed Information");
            setEnabled(false);
        }

        public void actionPerformed(ActionEvent event) {

            StringBuffer message = new StringBuffer();
            message.append("<html>");
            message.append("<b>Name:</b> ");
            message.append(getFeed().getName());
            message.append("<br>");
            message.append("<b>Site URL:</b> ");
            message.append(getFeed().getSiteUrl());
            message.append("<br>");
            message.append("<b>Feed URL:</b> ");
            message.append(getFeed().getFeedUrl());
            message.append("</html>");

            JOptionPane.showMessageDialog(null, message.toString());
        }
    }

    public static void main(String[] a){
      JFrame f = new JFrame("How To Validate Example");
      f.setDefaultCloseOperation(2);
      f.add(new ValidationHowExample());
      f.pack();
      f.setVisible(true);
    }
}


           
       





 

 




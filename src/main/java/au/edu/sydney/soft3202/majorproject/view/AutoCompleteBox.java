
package au.edu.sydney.soft3202.majorproject.view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.control.ComboBox;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;


public class AutoCompleteBox implements EventHandler<KeyEvent>{
    private ComboBox comboBox;
    final private ObservableList data;
    private Integer sid;


    /**
     * Constructor
     * @param comboBox
     */
    public AutoCompleteBox(final ComboBox comboBox) {
        this.comboBox = comboBox;
        this.data = comboBox.getItems();
        this.AutoComplete();
    }


    /**
     * Set some event to the autocomplete box
     */
    private void AutoComplete() {
        this.comboBox.setEditable(true);
        this.comboBox.getEditor().focusedProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue){
                this.comboBox.show();
            }
        });

        this.comboBox.getEditor().setOnMouseClicked(event ->{
            if(event.getButton().equals(MouseButton.PRIMARY)){
                if(event.getClickCount() == 2){
                    return;
                }
            }
            this.comboBox.show();
        });

        this.comboBox.setOnKeyPressed(t -> comboBox.hide());

        this.comboBox.setOnKeyReleased(AutoCompleteBox.this);

        if(this.sid!=null)
            this.comboBox.getSelectionModel().select(this.sid);
    }

    /**
     * Override handle method to handleEvent(key event)
     * @param event
     */
    @Override
    public void handle(KeyEvent event) {
        if(event.getCode() == KeyCode.ENTER && comboBox.getSelectionModel().getSelectedIndex()>-1)
            return;

        setItems();
    }

    /**
     * This method is used to set items
     */
    private void setItems() {
        ObservableList list = FXCollections.observableArrayList();

        for (Object datum : this.data) {
            String s = this.comboBox.getEditor().getText().toLowerCase();
            if (datum.toString().toLowerCase().contains(s.toLowerCase())) {
                list.add(datum.toString());
            }
        }

        if(list.isEmpty()) this.comboBox.hide();
        this.comboBox.setItems(list);
        this.comboBox.show();
    }

}
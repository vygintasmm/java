package Sample.fxControl;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class SignUpForm implements Initializable {
    public TextField nameField;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void returnToLogin(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxml/login.fxml"));
        Parent root = loader.load();

        Stage stage = (Stage) nameField.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }
}

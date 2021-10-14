package Sample.fxControl;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.Callback;
import Sample.utils.DbOperations;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

// 1. Kaip issaugo localdate, nes dabar lenteleje yra string; Update
// 2. Sutvarkyti koda ir iskelti db operacijas prie utilities ar db operations
// 3. Naujo kurso sukurimas: Prisideti labels, id kiekvienam laukui priskirti, mygtuko id + veiksmas
// 4. Naujo kurso sukurimas: metodas, kuriame bus INSERT INTO courses tas reiksmes, kurias suvede per forma.

public class MainWindow implements Initializable {
    @FXML
    public ListView myCourses;
    @FXML
    public ListView allCourses;
    @FXML
    public TableView myCreatedCourses;


    public TableColumn<TableParams, Integer> colId;
    public TableColumn<TableParams, String> colName;
    public TableColumn<TableParams, String> colStart;
    public TableColumn<TableParams, String> colEnd;
    public TableColumn<TableParams, Number> colPrice;
    public TableColumn<TableParams, String> deleteCol;
    private ObservableList<TableParams> data = FXCollections.observableArrayList();

    private int courseIS;
    private String currentUser;
    private Connection connection;
    private PreparedStatement statement;


    public void setFormData(int courseIS, String loginName) throws SQLException {
        this.courseIS = courseIS;
        this.currentUser = loginName;

        fillWithData();
    }

    private void fillWithData() throws SQLException {
        allCourses.getItems().clear();
        //connect and fill with data from db
        connection = DbOperations.connectToDb();
        String sql = "SELECT * FROM course AS c WHERE c.course_is = ?";
        statement = connection.prepareStatement(sql);
        statement.setInt(1, courseIS);
        ResultSet rs = statement.executeQuery();
        while (rs.next()) {
        }
        DbOperations.disconnectFromDb(connection, statement);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //Tableview structure and behaviour
        myCreatedCourses.setEditable(true);
        colId.setCellValueFactory(new PropertyValueFactory<TableParams, Integer>("colId"));
        colName.setCellValueFactory(new PropertyValueFactory<TableParams, String>("colName"));
        colName.setCellFactory(TextFieldTableCell.forTableColumn());
        colName.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<TableParams, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<TableParams, String> t) {
                ((TableParams) t.getTableView().getItems().get(
                        t.getTablePosition().getRow())
                ).setColName(t.getNewValue());
                TableParams tp = (TableParams) t.getTableView().getItems().get(
                        t.getTablePosition().getRow());
                try {
                    updateDbRecord(tp.getColId(), "name", tp.getColName());
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }

        });

        colStart.setCellValueFactory(new PropertyValueFactory<TableParams, String>("colStart"));
        colStart.setCellFactory(TextFieldTableCell.forTableColumn());
        colStart.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<TableParams, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<TableParams, String> t) {
                ((TableParams) t.getTableView().getItems().get(
                        t.getTablePosition().getRow())
                ).setColStart(t.getNewValue());
                TableParams tp = (TableParams) t.getTableView().getItems().get(
                        t.getTablePosition().getRow());
                try {
                    updateDbRecord(tp.getColId(), "start_date", tp.getColStart());
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        });

        colEnd.setCellValueFactory(new PropertyValueFactory<TableParams, String>("colEnd"));
        colEnd.setCellFactory(TextFieldTableCell.forTableColumn());
        colEnd.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<TableParams, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<TableParams, String> t) {
                ((TableParams) t.getTableView().getItems().get(
                        t.getTablePosition().getRow())
                ).setColEnd(t.getNewValue());
                TableParams tp = (TableParams) t.getTableView().getItems().get(
                        t.getTablePosition().getRow());
                try {
                    updateDbRecord(tp.getColId(), "end_date", tp.getColEnd());
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        });

        colPrice.setCellValueFactory(new PropertyValueFactory<TableParams, Number>("colPrice"));
        colPrice.setCellFactory(tc -> new TableCell<TableParams, Number>());
        colPrice.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<TableParams, Number>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<TableParams, Number> t) {
                ((TableParams) t.getTableView().getItems().get(
                        t.getTablePosition().getRow())
                ).setColPrice((Double) t.getNewValue());
                TableParams tp = (TableParams) t.getTableView().getItems().get(
                        t.getTablePosition().getRow());
                try {
                    updateDbRecord(tp.getColId(), "course_price", tp.getColPrice());
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        });

        deleteCol.setCellValueFactory(new PropertyValueFactory<>("DUMMY"));

        Callback<TableColumn<TableParams, String>, TableCell<TableParams, String>> cellFactory
                = //
                new Callback<TableColumn<TableParams, String>, TableCell<TableParams, String>>() {
                    @Override
                    public TableCell call(final TableColumn<TableParams, String> param) {
                        final TableCell<TableParams, String> cell = new TableCell<TableParams, String>() {

                            final Button btn = new Button("Delete");

                            @Override
                            public void updateItem(String item, boolean empty) {
                                super.updateItem(item, empty);
                                if (empty) {
                                    setGraphic(null);
                                    setText(null);
                                } else {
                                    btn.setOnAction(event -> {

                                        TableParams tp = (TableParams) getTableView().getItems().get(getIndex());
                                        try {
                                            deleteDbRecord(tp.getColId());
                                        } catch (SQLException throwables) {
                                            throwables.printStackTrace();
                                        }
                                    });
                                    setGraphic(btn);
                                    setText(null);
                                }
                            }
                        };
                        return cell;
                    }
                };
        deleteCol.setCellFactory(cellFactory);


    }

    public void enroll(ActionEvent actionEvent) {
    }

    public void viewCourseInfo(ActionEvent actionEvent) {
    }

    public void loadCreatedCourses(Event event) throws SQLException {
        myCreatedCourses.getItems().clear();
        //get from db
        refresh();
    }

    private void refresh() throws SQLException {
        connection = DbOperations.connectToDb();
        String sql = "SELECT c.id, c.name, c.start_date, c.end_date, c.course_price FROM course AS c, users as U WHERE c.admin_id = u.id AND u.login = ?";
        statement = connection.prepareStatement(sql);
        statement.setString(1, currentUser);
        ResultSet rs = statement.executeQuery();
        while (rs.next()) {
            TableParams tableParams = new TableParams();
            tableParams.setColId(rs.getInt(1));
            tableParams.setColName(rs.getString(2));
            tableParams.setColStart(rs.getString(3));
            tableParams.setColEnd(rs.getString(4));
            tableParams.setColPrice(rs.getDouble(5));
            data.add(tableParams);
        }
        myCreatedCourses.setItems(data);
        DbOperations.disconnectFromDb(connection, statement);
    }
    public void commitDb(TableColumn.CellEditEvent<TableParams, String> tableParamsStringCellEditEvent) {

    }

    private void updateDbRecord(int id, String colName, Double newValue) throws SQLException {
        connection = DbOperations.connectToDb();
        String sql = "UPDATE course SET `" + colName + "`  = ? WHERE id = ?";
        statement = connection.prepareStatement(sql);
        statement.setDouble(1, newValue);
        statement.setInt(2, id);
        statement.executeUpdate();
        DbOperations.disconnectFromDb(connection, statement);
    }

    private void updateDbRecord(int id, String colName, String newValue) throws SQLException {
        connection = DbOperations.connectToDb();
        String sql = "UPDATE course SET `" + colName + "`  = ? WHERE id = ?";
        statement = connection.prepareStatement(sql);
        statement.setString(1, newValue);
        statement.setInt(2, id);
        statement.executeUpdate();
        DbOperations.disconnectFromDb(connection, statement);
        refresh();
    }

    private void deleteDbRecord(int id) throws SQLException {
        connection = DbOperations.connectToDb();
        String sql = "DELETE FROM course WHERE id = ?";
        statement = connection.prepareStatement(sql);
        statement.setInt(1, id);
        statement.executeUpdate();
        DbOperations.disconnectFromDb(connection, statement);
        refresh();
    }
}

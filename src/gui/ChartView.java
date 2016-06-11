package gui;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.GridPane;
import jfx.messagebox.MessageBox;
import model.entities.Drug;
import model.entities.PriceChange;
import model.sql.SqlDriver;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.stage.Stage;

import java.util.List;


public class ChartView extends Application {

    SqlDriver driver = SqlDriver.getInstance();
    List<Drug> drugs;

    public ChartView(List<Drug> drugs) {
        this.drugs = drugs;
    }

    @Override public void start(Stage primaryStage) {

        Stage stage = new Stage();
        //TODO: Заменить "НАЗВАНИЕ ПРИЛОЖУХИ" на название приложухи
        stage.setTitle("НАЗВАНИЕ ПРИЛОЖУХИ");


        //defining the axes
        final NumberAxis yAxis = new NumberAxis();
        final CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("Дата");
        yAxis.setLabel("Цена");

        // define grid
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        // creating favorites combobox
        final ComboBox comboBox = new ComboBox();
        for (Drug drug : drugs) {
            comboBox.getItems().add(drug.getName());
        }

        grid.add(comboBox, 0, 0);

        //creating the chart
        final LineChart<String, Number> lineChart =
                new LineChart<String, Number>(xAxis, yAxis);
        grid.add(lineChart, 0, 1);


        comboBox.valueProperty().addListener(new ChangeListener<String>() {
            @Override public void changed(ObservableValue ov, String t, String t1) {
                try {
                    int index = comboBox.getSelectionModel().getSelectedIndex();
                    Drug drug = drugs.get(index);
                    drugSelected(drug, lineChart);
                }
                catch(Exception ex) {
                    System.out.println(ex.getLocalizedMessage());
                    showError(stage, "Не удалось выбрать лекарство");
                }
            }
        });

        Scene scene  = new Scene(grid,1000,750);
        stage.setScene(scene);
        stage.show();
    }

    public void drugSelected(Drug drug, LineChart chart) throws Exception {
        //Set title of the chart
        chart.setTitle("Изменение цен на лекарсатво " + drug.getName());


        //defining a series
        XYChart.Series series = new XYChart.Series();

        //Set series's name ВЫГЛЯДИТ ФУ КАК УБАТЬ ВООБЩЕ ХЗ
        //series.setName("");


        fillData(series.getData(), drug);

        chart.getData().clear();
        chart.getData().add(series);
    }

    private void fillData(ObservableList<XYChart.Data> data, Drug drug) throws Exception {
        List<PriceChange> list = driver.findPriceChanges(drug);
        for (PriceChange i : list){
            data.add(new XYChart.Data(i.getString(), i.getPriceDouble()));
        }
    }


    private void showError(Stage stage, String message) {
        MessageBox.show(stage,
                message,
                "Ошибка",
                MessageBox.ICON_ERROR);
    }
}



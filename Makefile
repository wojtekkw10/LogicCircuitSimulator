run:
	gradle shadowjar
	java --module-path javafx-sdk-11.0.2/lib/ --add-modules=javafx.controls,javafx.fxml -jar build/libs/InternalModel-all.jar

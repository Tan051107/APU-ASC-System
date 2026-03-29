package dto;

public class AddCarDto {
    private String carModel;
    private String carPlate;

    public AddCarDto(String carModel, String carPlate) {
        this.carModel = carModel;
        this.carPlate = carPlate;
    }

    public String getCarModel() {
        return carModel;
    }

    public void setCarModel(String carModel) {
        this.carModel = carModel;
    }

    public String getCarPlate() {
        return carPlate;
    }

    public void setCarPlate(String carPlate) {
        this.carPlate = carPlate;
    }
}

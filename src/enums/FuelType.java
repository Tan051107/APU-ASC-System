package enums;

public enum FuelType {
    PETROL ("Petrol"),
    DIESEL("Diesel"),
    HYBRID ("Hybrid"),
    ELECTRIC ("Electric"),
    PLUG_IN_HYBRID("Plug-in Hybrid");

    private final String displayFuelType;

    FuelType(String displayFuelType){
        this.displayFuelType = displayFuelType;
    }
    public String toString(){
        return displayFuelType;
    }

    public static FuelType fromString(String displayFuelType){
        for(FuelType fuelType : FuelType.values()){
            if(fuelType.displayFuelType.equalsIgnoreCase(displayFuelType)){
                return fuelType;
            }
        }
        throw new IllegalArgumentException("Invalid fuel type");
    }

}

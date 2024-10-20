package model;

public class NoCard implements RenterType {
    @Override
    public int maxVolumes(int volumes) {  // Now accepts the parameter directly
        return volumes;
    }

    @Override
    public String getRenterTypeInfo() {
        return "No Card";
    }
}

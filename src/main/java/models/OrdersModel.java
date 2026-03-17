package models;

import java.util.List;

public class OrdersModel {
    private  List<String> ingredients;
    public OrdersModel(List<String> ingredients){this.ingredients=ingredients;}
    public OrdersModel(){}

    public List<String> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<String> ingredients) {
        this.ingredients = ingredients;
    }
}

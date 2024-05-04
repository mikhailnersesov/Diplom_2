package ru.praktikum.diplom.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.json.JSONArray;

import java.util.ArrayList;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrdersIngredientsRequest {
    private ArrayList<String> ingredients;
}

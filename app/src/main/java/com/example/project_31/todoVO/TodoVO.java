package com.example.project_31.todoVO;

import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TodoVO {

    private String Category;
    private ArrayList Category1;
    private ArrayList TodoList;
    private int position;
    private LinearLayout CategoryLayout;
    private HashMap<String, List> categoryAndtodoList = new HashMap<>();


    public String getCategory() {
        return Category;
    }

    public void setCategory(String category) {
        this.Category = category;
    }

    public ArrayList getTodoList() {
        return TodoList;
    }

    public void setCategory1(ArrayList category1) {
        Category1 = category1;
    }

    public ArrayList getCategory1() {
        return Category1;
    }

    public void setTodoList(ArrayList todoList) {
        this.TodoList = todoList;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public LinearLayout getCategoryLayout() {
        return CategoryLayout;
    }

    public void setCategoryLayout(LinearLayout categoryLayout) {
        this.CategoryLayout = categoryLayout;
    }

    public HashMap getCategoryAndtodoList() {
        return categoryAndtodoList;
    }

    public void setCategoryAndtodoList(String categoryKey, List<String> values) {
        categoryAndtodoList.put(categoryKey,values);
    }
}
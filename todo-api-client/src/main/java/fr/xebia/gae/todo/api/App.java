package fr.xebia.gae.todo.api;

import com.appspot.todo_endpoint.todos.Todos;
import com.appspot.todo_endpoint.todos.model.*;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;

import java.io.IOException;

public class App 
{
    public static void main( String[] args ) throws IOException {
        HttpTransport httpTransport = new NetHttpTransport();
        JsonFactory jsonFactory = new JacksonFactory();

        Todos.Builder builder = new Todos.Builder(httpTransport, jsonFactory, null);
        builder.setRootUrl("https://todo-endpoint.appspot.com/_ah/api");
        Todos todosApi = builder.build();

        Todo newTodo = new Todo();
        newTodo.setTitle("hello");
        todosApi.create(newTodo).execute();

        newTodo = new Todo();
        newTodo.setTitle("world");
        todosApi.create(newTodo).execute();

        TodoCollection todoCollection = todosApi.list().execute();
        for (Todo todo : todoCollection.getItems()) {
            System.out.println(todo.getTitle() + "(" + todo.getId() + ")");
            if (todo.getTitle().equals("hello")) {
                todosApi.remove(todo.getId());
                System.out.println("=> deleted");
            }
            if (todo.getTitle().equals("world")) {
                todosApi.remove(todo.getId()).execute();
                System.out.println("=> deleted");
            }
        }
    }
}

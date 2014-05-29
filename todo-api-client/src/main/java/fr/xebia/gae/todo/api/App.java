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
        todosApi.create(newTodo);

        newTodo = new Todo();
        newTodo.setTitle("world");
        todosApi.create(newTodo);

        TodoCollection todoCollection = todosApi.list().execute();
        System.out.println(todoCollection.getItems());


    }
}

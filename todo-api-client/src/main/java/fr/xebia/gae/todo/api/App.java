package fr.xebia.gae.todo.api;

import com.appspot.todo_endpoint.todo.Todo;
import com.appspot.todo_endpoint.todo.model.TodoCollection;
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


        Todo.Builder builder = new Todo.Builder(httpTransport, jsonFactory, null);
        builder.setRootUrl("http://localhost:8080/_ah/api/");
        Todo todoService = builder.build();
        TodoCollection todoCollection = todoService.list().execute();
        System.out.println(todoCollection.getItems());
    }
}

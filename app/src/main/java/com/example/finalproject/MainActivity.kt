package com.example.finalproject

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.finalproject.ui.theme.FinalProjectTheme
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.gotrue.Auth
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable
import java.sql.Date
import androidx.compose.material3.ListItem
import androidx.compose.runtime.mutableStateListOf

val supabase = createSupabaseClient(
    supabaseUrl = "https://tinxwlddxbzbozbqtneh.supabase.co",
    supabaseKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6InRpbnh3bGRkeGJ6Ym96YnF0bmVoIiwicm9sZSI6ImFub24iLCJpYXQiOjE3MTg4MDQ3MzEsImV4cCI6MjAzNDM4MDczMX0.RvEvWTvOesonHXv6v9jWwJXDFSmeoP6Zw0dD5gm7gE4"
) {
    install(Auth)
    install(Postgrest)
    //install other modules
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FinalProjectTheme {
                BookList()
            }
        }
    }
}

@Serializable
data class Book (
    val id: Int,
    val title: String,
    val author: String,
    val date: String
)

@Composable
fun BookList(){
   val book = remember {
       mutableStateListOf<Book>()
   }

    LaunchedEffect(Unit) {
        withContext(Dispatchers.IO){
            val response = supabase.postgrest.from("book").select().decodeList<Book>()
           book.addAll(response)
        }
    }
    LazyColumn {
        items(book) { item ->
            ListItem(headlineContent = { Text(text = item.title) })
        }
    }
}
package com.example.finalproject.presentation

import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember

import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.gotrue.Auth
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

import androidx.compose.material3.ListItem
import androidx.compose.runtime.mutableStateListOf
import com.example.finalproject.model.shopping.BookDTO
import com.example.finalproject.BuildConfig
import dagger.hilt.android.AndroidEntryPoint

val supabase = createSupabaseClient(
    supabaseUrl = BuildConfig.SUPABASE_URL,
    supabaseKey = BuildConfig.SUPABASE_ANON_KEY
) {
    install(Auth)
    install(Postgrest)
    //install other modules
}

//class MainActivity : ComponentActivity() {
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
//        setContent {
//            FinalProjectTheme {
//                BookList()
//            }
//        }
//    }
//}
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    //This will come later
}


@Composable
fun BookList(){
   val book = remember {
       mutableStateListOf<BookDTO>()
   }

    LaunchedEffect(Unit) {
        withContext(Dispatchers.IO){
            try {
                val response = supabase.postgrest.from("book").select().decodeList<BookDTO>()
                book.addAll(response)
            } catch (e: Exception) {
                println("Error: ${e.message}")
            }
        }
    }

    LazyColumn {
        items(book.chunked(3)) { rowBooks ->
            Row(horizontalArrangement = Arrangement.SpaceEvenly) {
                for (item in rowBooks) {
                    ListItem(headlineContent = { Text(text = item.title) })
                }
            }
        }
    }
}
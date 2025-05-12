// app/src/main/java/com/example/tempexpirymate/MainActivity.kt
package com.example.tempexpirymate




import android.os.Build
import android.os.Bundle
import android.widget.SlidingDrawer
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SmallTopAppBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.example.tempexpirymate.ui.theme.TempExpiryMateTheme
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.DatePicker
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.tempexpirymate.ui.theme.Dimens
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import androidx.compose.runtime.*
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import java.time.Instant
import java.time.ZoneId
import androidx.compose.material3.Button
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.ui.window.DialogProperties
import androidx.compose.foundation.combinedClickable
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import kotlin.math.exp
import androidx.compose.foundation.ExperimentalFoundationApi




data class ExpiryItem(
    val id:Long,
    val name: String,
    val expiryText:String,
    val statusColor: Color
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TempExpiryMateTheme {
                ExpiryMateApp()
            }
        }
    }
}
@Composable
fun ExpiryMateApp() {
    val vm: ExpiryViewModel=viewModel()

    val nav = rememberNavController()
    val items by vm.items.collectAsState()

    NavHost(navController = nav, startDestination = "main") {
        composable("main") {
            MainScreen(
                items=items,
                onAddClicked = { nav.navigate("addItem") },
                onDelete = {id->vm.deleteItem(id)},
                onReset = {id->vm.resetItemDate(id)},
                onEdit ={id->nav.navigate("edit/$id")} )
        }
        composable("addItem") {
            //Placeholder “Add Item” screen
            AddItemScreen(
                onSave = { name, date, color ->
                    vm.addItem(name,date,color)
                    nav.popBackStack() },
                onBack = { nav.popBackStack() }
            )
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    items:List<ExpiryItem>, onAddClicked :()-> Unit,
    onDelete:(Long)-> Unit,
    onReset: (Long) -> Unit,
    onEdit: (Long) -> Unit) {
    Scaffold(
        topBar = {
            SmallTopAppBar(
                title   = { Text("ExpiryMate") },
                actions = {
                    IconButton(onClick = { /* TODO */ }) {
                        Icon(Icons.Default.Search, contentDescription = "Search")
                    }
                }
            )
        },
        floatingActionButton = {
            //2b).Our "+" FAB
            FloatingActionButton(onClick = onAddClicked){
                Icon(Icons.Default.Add, contentDescription = "Add Item")
            }
        }){
         Padding ->
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(Padding)
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ){
                items(items){item->
                    ExpiryRow(
                        item=item,
                        onDelete = onDelete,
                        onReset = onReset,
                        onEdit = onEdit)
                }
        }
        }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ExpiryRow(item: ExpiryItem,
              onDelete:(Long)-> Unit,
              onReset:(Long)-> Unit,
              onEdit:(Long)-> Unit){
    var menuExpanded by remember { mutableStateOf(false) }


    Surface (
        //1)Card container
        color=MaterialTheme.colorScheme.surfaceVariant,
        tonalElevation = Dimens.cardElevation,
        shape = MaterialTheme.shapes.medium,
        modifier = Modifier.fillMaxWidth().padding(vertical = Dimens.cardElevation)
            .combinedClickable(
                onClick = {/*optional tap */},
                onLongClick = {menuExpanded=true}
            )

    ){
        //2)Horizontal layout for icon,texts, and dot
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(Dimens.rowInnerPadding)
        ){
            //2.1 Icon placeholder (40x40 Grey Box)
            Box(
                modifier = Modifier.size(Dimens.iconSize)
                    .clip(MaterialTheme.shapes.small)
                    .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f))
            )

            //2.2 Spacing between Icon and Text
            Spacer(Modifier.width(Dimens.rowItemSpacing))

            //2.3 Text Column
            Column (modifier= Modifier.weight(1f)){
                Text(
                    text=item.name,
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text=item.expiryText,
                    style = MaterialTheme.typography.bodyMedium
                )

            }
            //2.4 Status dot
            Spacer(Modifier.width(Dimens.rowItemSpacing))
            Box(
                modifier = Modifier.size(Dimens.dotSize)
                    //5.1 Add a 1 dp border
                    .border(
                    BorderStroke(1.dp, MaterialTheme.colorScheme.onSurface.copy(alpha=0.3f)),
                    CircleShape
                )
                    // 5.2 Fill with status color
                    .background(item.statusColor, CircleShape)
            )
        }

    }
    DropdownMenu(
        expanded = menuExpanded,
        onDismissRequest = {menuExpanded=false}
    ) {
        DropdownMenuItem(
            text={Text("Delete")},
            onClick = {
                menuExpanded=false
                onDelete(item.id)
            }
        )

        DropdownMenuItem(
            text = {Text("Reset Date")},
            onClick = {
                menuExpanded=false
                onReset(item.id)
            }
        )
        DropdownMenuItem(
            text={Text("Edite")},
            onClick = {
                menuExpanded=false
                onEdit(item.id)
            }
        )
    }

}
@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddItemScreen(
    onBack:()-> Unit,
    onSave:(name : String, date:LocalDate,color:Color)-> Unit
){
    //3a)State holders
    var name by remember{ mutableStateOf("") }
    // A nullable LocalDate: starts out null, and Compose will recompose when it changes
    var pickedDate by remember {mutableStateOf<LocalDate?>(null)}
    var showPicker by remember {mutableStateOf(false)}
    var selectedColor by remember{mutableStateOf(Color(0xFF4CAF50))} //default green

    //remember a datePickerState
    val datePickerState= rememberDatePickerState()

    //3b)DatePickerDialog (Material3)
    if(showPicker){
       DatePickerDialog(
           onDismissRequest={showPicker=false},


           //Your Own Button
           confirmButton = {
               TextButton(onClick= {
                   showPicker=false
                   datePickerState.selectedDateMillis?.let { epoch->
                       pickedDate= Instant
                           .ofEpochMilli(epoch)
                           .atZone(ZoneId.systemDefault())
                           .toLocalDate()
                   }
               }){
           Text("OK")
           }
           },

           //Own cancel Button
       dismissButton = {
           TextButton(onClick={showPicker=false}) {
               Text("Cancel ")
           }
       },
           properties = DialogProperties(
               usePlatformDefaultWidth = false,
               dismissOnBackPress = true, //allow back button to close
               dismissOnClickOutside = true //allow tapping out side to close


           )

       ){
           //Date picker itself
           DatePicker(state=datePickerState)
       }
    }
    Scaffold (
        topBar={
            SmallTopAppBar(
                navigationIcon={
                    IconButton(onClick = onBack){
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                title={Text("Add Item")}
            )

        },
        content={inner->
            Column(
                Modifier.fillMaxSize().padding(inner)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ){
                //Name field
                OutlinedTextField(
                    value=name,
                    onValueChange = {name=it},
                    label = { Text("Name") },
                    modifier= Modifier.fillMaxWidth(),
                    shape = MaterialTheme.shapes.medium

                )
                //1)Date field(read-only,taps open picker)
                OutlinedTextField(
                    value = pickedDate?.format(DateTimeFormatter.ofPattern("MMM d, yyyy")) ?:"",
                    onValueChange = {/*no-op,read only*/ },
                    readOnly = true,
                    label = {Text("Date")},
                    trailingIcon = {
                        IconButton(onClick = {showPicker=true}) {
                            Icon(Icons.Default.DateRange, contentDescription = "Pick Date")
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape=MaterialTheme.shapes.medium //give it round corner
                )

                //3).Colored Choices
                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)){
                    listOf(Color.Red,Color(0xFFFFA500),Color(0xFF4CAF50)).forEach { c->
                        Box(
                            Modifier.size(40.dp)
                                .clip(CircleShape)
                                .background(c)
                                .border(
                                    width=if(c==selectedColor)3.dp else 1.dp,
                                    color = if(c==selectedColor) MaterialTheme.colorScheme.primary
                                    else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f),
                                    shape=CircleShape
                                )
                                .clickable { selectedColor = c }
                        )
                    }
                }
                //Save Button
                Button(
                    onClick={
                        pickedDate?.let{onSave(name,it,selectedColor)} },
                    enabled=name.isNotBlank() && pickedDate!=null,
                    modifier= Modifier.fillMaxWidth()
                ){
                    Text("Save")
                }
            }
        }

        )
}


@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    TempExpiryMateTheme {
        MainScreen(items = emptyList(), onAddClicked = {}, onDelete = {}, onReset = {}, onEdit = {})
    }
}


package com.example.jetpackapp

import android.app.Application
import android.content.Context
import android.os.Bundle
import android.os.PersistableBundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.absoluteOffset
import androidx.compose.foundation.layout.absolutePadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.currentCompositionLocalContext
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.MutableCreationExtras
import com.example.jetpackapp.ui.theme.JetpackAppTheme
import java.util.function.Function

class MainActivity : ComponentActivity() {

    private var lv = MutableLiveData<Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            JetpackAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Greeting(this, lv = lv)
                }
            }
        }

//        val factory = PersonVMFactory(MyApp.mInstance)
//        val vm = ViewModelProvider(this, factory = factory)[PersonViewModel::class.java]
//        vm.lv.observe(this, {
//
//        })

        //switchMap用法
//        val newLv: LiveData<String> = Transformations.switchMap(lv) { getMyLiveData(it) }
//        newLv.observe(this) { Toast.makeText(this, it, Toast.LENGTH_SHORT).show() }

        //map用法
        val result = Transformations.map(lv){
            when (it) {
                101->"张三"
                102->"李四"
                103->"王二麻"
                else->"二逼"
            }
        }
        result.observe(this) {
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
        }
    }

    private fun getMyLiveData(p: Int): LiveData<String> {
        val str = when (p) {
            100 -> "xiaomi"
            101 -> "huawei"
            102 -> "samsung"
            103 -> "lenovo"
            else -> "null"
        }
        return MutableLiveData(str)
    }
}

@Composable
fun Greeting(
    context: Context?,
    modifier: Modifier = Modifier,
    lv: MutableLiveData<Int> = MutableLiveData<Int>(200)
) {
    val context = context as MainActivity
    val factory = PersonVMFactory(MyApp.mInstance)
    val myViewModel: PersonViewModel<Int> =
        ViewModelProvider(
            context,
            factory = factory
        )[PersonViewModel::class.java] as PersonViewModel<Int>
    var data by remember { mutableStateOf(myViewModel.total) }
    Column(
        modifier = Modifier
            .background(color = Color.Gray)
            .size(300.dp, 400.dp)
            .absolutePadding(top = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "myViewModel.total=${myViewModel.total}, $data",
            modifier = Modifier.absolutePadding(bottom = 20.dp),
        )
        Button(
            onClick = {
                myViewModel.total = (myViewModel.total as Int).plus(1)
                data++
                lv.value = 101
            }) {
            Text(text = "huawei")
        }
        Button (
                onClick = {
                    lv.value = 102
                }) {
            Text(text = "samsung")
        }
        Button (
                onClick = {
                    lv.value = 103
                }) {
            Text(text = "lenovo")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    JetpackAppTheme {
        Greeting(null)
    }
}

class MyViewModel(var total: String, private val context: Application) : AndroidViewModel(context)

class PersonViewModel<T>(var total: T) : ViewModel() {
    val lv by lazy { MutableLiveData(total) }
    fun change(t: T) {
        lv.value = t
    }
}

class PersonVMFactory(context: Application) : ViewModelProvider.AndroidViewModelFactory(context) {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val perClass = PersonViewModel::class.java
        if (modelClass.isAssignableFrom(perClass)) {
            return PersonViewModel(100) as T
        }
        return super.create(modelClass)
    }
}
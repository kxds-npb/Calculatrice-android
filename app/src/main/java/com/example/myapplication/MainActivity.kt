package com.example.myapplication


import android.annotation.SuppressLint
import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Arrangement.Center
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.ui.theme.MyApplicationTheme
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myapplication.ui.theme.CalculatorViewModel
import com.example.myapplication.ui.theme.Purple40
import com.example.myapplication.ui.theme.jaune

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                Surface {
                    Detection()
                }
            }
        }
    }
}

@Composable
fun Detection() {
    val viewModel: CalculatorViewModel = viewModel() // ← ICI
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

    if(isLandscape) {
        Landscape(viewModel) // écran retourné
    } else {
        Portrait(viewModel)  // écran droit
    }
}

@Composable
fun Portrait(viewModel: CalculatorViewModel) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {
        Affichage(chaine = viewModel.chaine, actuel = viewModel.actuel)
        BouttonsPortrait(viewModel =  viewModel)
    }
}

@Composable
fun Landscape(viewModel: CalculatorViewModel) {
    Column(modifier = Modifier
        .statusBarsPadding()
        .navigationBarsPadding()
    ) {
        Affichage(chaine = viewModel.chaine, actuel = viewModel.actuel)
        BouttonsLandscape(viewModel =  viewModel)
    }
}



@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun Affichage(chaine: String, actuel: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.35f)
            .padding(horizontal = 8.dp),
        verticalArrangement = Center
    ) {
        Text(
            text = chaine,
            textAlign = TextAlign.End,
            fontSize = 29.sp,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(41.dp))

        BoxWithConstraints(
            modifier = Modifier.fillMaxWidth()
        ) {
            // Calcul dynamique basé sur la longueur du texte
            val fontSize by remember(actuel) {
                mutableStateOf(
                    when {
                        actuel.length >= 15 -> 27.sp
                        actuel.length >= 12 -> 32.sp
                        actuel.length >= 10 -> 37.sp
                        else -> 44.sp
                    }
                )
            }

            Text(
                text = actuel,
                textAlign = TextAlign.End,
                fontSize = fontSize,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                softWrap = false,
                overflow = TextOverflow.Visible,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = jaune)
            )
        }
        
    }
}

@Composable
fun MonBouton(
    text : String,
    onClick: () -> Unit
){
  Button(onClick = onClick,
      modifier = Modifier.height(80.dp)
          .padding(8.dp),
      shape = RoundedCornerShape(50.dp),
  )
  { Text(text = text, fontSize = 28.sp) }
}


@Composable
fun BouttonsLandscape(viewModel: CalculatorViewModel) {
    val chaine = viewModel.chaine
    Column(
        modifier = Modifier
            .statusBarsPadding()
            .navigationBarsPadding()
            .fillMaxSize()
            .padding(8.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(color = Purple40)
            .padding(8.dp),
        verticalArrangement = Arrangement.spacedBy(15.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(
                onClick = { viewModel.effacer() },
                modifier = Modifier
                    .weight(0.4f)
                    .padding(horizontal = 9.dp)
                    .height(35.dp) // Boutons plus hauts
            ) {
                Text("C", fontSize = 20.sp, fontWeight = FontWeight.Bold) // Texte plus grand
            }
            Button(
                onClick = { viewModel.ajouterCaractere("/") },
                modifier = Modifier
                    .weight(0.4f)
                    .padding(horizontal = 9.dp)
                    .height(35.dp) // Boutons plus hauts

            ) {
                Text("/",fontSize = 20.sp, fontWeight = FontWeight.Bold)
            }
            Button(
                onClick = { viewModel.ajouterCaractere("x") },
                modifier = Modifier
                    .weight(0.4f)
                    .padding(horizontal = 9.dp)
                    .height(35.dp)

            ) {
                Text("x", fontSize = 20.sp, fontWeight = FontWeight.Bold)
            }

            Button(
                onClick = { viewModel.supprimerValeur()
                    viewModel.calculer()
                    if (chaine.length == 1){
                        viewModel.effacer()
                    }
                },
                modifier = Modifier
                    .weight(0.4f)
                    .padding(horizontal = 9.dp)
                    .height(35.dp)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "back",
                    modifier = Modifier.size(50.dp),
                )
            }

        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(
                onClick = { viewModel.ajouterCaractere("7")
                    viewModel.calculer()},
                modifier = Modifier
                    .weight(0.4f)
                    .padding(horizontal = 9.dp)
                    .height(35.dp)
            ) {
                Text("7", fontSize = 20.sp, fontWeight = FontWeight.Bold)
            }
            Button(
                onClick = { viewModel.ajouterCaractere("8")
                    viewModel.calculer()},
                modifier = Modifier
                    .weight(0.4f)
                    .padding(horizontal = 9.dp)
                    .height(35.dp)
            ) {
                Text("8", fontSize = 20.sp, fontWeight = FontWeight.Bold)
            }
            Button(
                onClick = { viewModel.ajouterCaractere("9")
                    viewModel.calculer()},
                modifier = Modifier
                    .weight(0.4f)
                    .padding(horizontal = 9.dp)
                    .height(35.dp)
            ) {
                Text("9", fontSize = 20.sp, fontWeight = FontWeight.Bold)
            }
            Button(
                onClick = { viewModel.ajouterCaractere("-") },
                modifier = Modifier
                    .weight(0.4f)
                    .padding(horizontal = 9.dp)
                    .height(35.dp)
            ) {
                Text("-", fontSize = 20.sp, fontWeight = FontWeight.Bold)
            }
            Button(
                onClick = { viewModel.ajouterCaractere("+")
                    viewModel.calculer()},
                modifier = Modifier
                    .weight(0.4f)
                    .padding(horizontal = 9.dp)
                    .height(35.dp)
            ) {
                Text("+", fontSize = 20.sp, fontWeight = FontWeight.Bold)
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(
                onClick = { viewModel.ajouterCaractere("4")
                    viewModel.calculer()},
                modifier = Modifier
                    .weight(0.4f)
                    .padding(horizontal = 9.dp)
                    .height(35.dp)
            ) {
                Text("4", fontSize = 20.sp, fontWeight = FontWeight.Bold)
            }
            Button(
                onClick = { viewModel.ajouterCaractere("5")
                    viewModel.calculer()},
                modifier = Modifier
                    .weight(0.4f)
                    .padding(horizontal = 9.dp)
                    .height(35.dp)
            ) {
                Text("5", fontSize = 20.sp, fontWeight = FontWeight.Bold)
            }
            Button(
                onClick = { viewModel.ajouterCaractere("6")
                    viewModel.calculer()},
                modifier = Modifier
                    .weight(0.4f)
                    .padding(horizontal = 9.dp)
                    .height(35.dp)
            ) {
                Text("6", fontSize = 20.sp, fontWeight = FontWeight.Bold)
            }

            Button(
                onClick = { viewModel.ajouterCaractere("%") },
                modifier = Modifier
                    .weight(0.4f)
                    .padding(horizontal = 9.dp)
                    .height(35.dp)
            ) {
                Text("%", fontSize = 20.sp, fontWeight = FontWeight.Bold)
            }

            Button(
                onClick = { viewModel.ajouterCaractere(".") },
                modifier = Modifier
                    .weight(0.4f)
                    .padding(horizontal = 9.dp)
                    .height(35.dp)
            ) {
                Text(".", fontSize = 20.sp, fontWeight = FontWeight.Bold)
            }

        }

        // 1 2 3 %
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            Button(
                onClick = { viewModel.ajouterCaractere("1")
                    viewModel.calculer()},
                modifier = Modifier
                    .weight(0.4f)
                    .padding(horizontal = 9.dp)
                    .height(35.dp)
            ) {
                Text("1", fontSize = 20.sp, fontWeight = FontWeight.Bold)
            }
            Button(
                onClick = { viewModel.ajouterCaractere("2")
                    viewModel.calculer()},
                modifier = Modifier
                    .weight(0.4f)
                    .padding(horizontal = 9.dp)
                    .height(35.dp)
            ) {
                Text("2", fontSize = 20.sp, fontWeight = FontWeight.Bold)
            }
            Button(
                onClick = { viewModel.ajouterCaractere("3") },
                modifier = Modifier
                    .weight(0.4f)
                    .padding(horizontal = 9.dp)
                    .height(35.dp)
            ) {
                Text("3", fontSize = 20.sp, fontWeight = FontWeight.Bold)
            }
            Button(
                onClick = { viewModel.ajouterCaractere("0")
                    viewModel.calculer()},
                modifier = Modifier
                    .weight(0.4f)
                    .padding(horizontal = 9.dp)
                    .height(35.dp)
            ) {
                Text("0", fontSize = 20.sp, fontWeight = FontWeight.Bold)
            }
            Button(
                onClick = { viewModel.calculer()
                    viewModel.egale() },
                modifier = Modifier
                    .weight(0.4f)
                    .padding(horizontal = 9.dp)
                    .height(35.dp)
            ) {
                Text("=", fontSize = 20.sp, fontWeight = FontWeight.Bold)
            }
        }

    }
}


@Composable
fun BouttonsPortrait(viewModel: CalculatorViewModel) {
    // Récupérer l'état depuis le ViewModel
    val chaine = viewModel.chaine

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 8.dp)
            .padding(bottom = 71.dp)

    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Purple40)
                .padding(top = 15.dp),
            verticalArrangement = Arrangement.SpaceAround
        ){
            Row(
                horizontalArrangement = Arrangement.SpaceAround,
                modifier = Modifier.fillMaxWidth()
            ) {

                MonBouton("C") {
                    viewModel.effacer()
                }
                MonBouton("/") {
                    viewModel.ajouterCaractere("/")
                }
                MonBouton("X") {
                    viewModel.ajouterCaractere("x")
                }

                Button(
                    onClick = { viewModel.supprimerValeur()
                        viewModel.calculer()
                        if (chaine.length == 1){
                            viewModel.effacer()
                        }
                    },
                    modifier = Modifier.padding(top = 7.dp),
                    shape = RoundedCornerShape(50.dp)

                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "back",
                        modifier = Modifier.height(50.dp).width(25.dp),
                    )
                }

            }

            Row(
                horizontalArrangement = Arrangement.SpaceAround ,
                modifier = Modifier.fillMaxWidth()
            ) {
                MonBouton("7") {
                    viewModel.ajouterCaractere("7")
                    viewModel.calculer()
                }
                MonBouton("8") {
                    viewModel.ajouterCaractere("8")
                    viewModel.calculer()
                }
                MonBouton("9") {
                    viewModel.ajouterCaractere("9")
                    viewModel.calculer()
                }
                MonBouton("-") {
                    viewModel.ajouterCaractere("-")
                }

            }


            Row(
                horizontalArrangement = Arrangement.SpaceAround ,
                modifier = Modifier.fillMaxWidth()
            ) {
                 MonBouton("4") {
                   viewModel.ajouterCaractere("4")
                   viewModel.calculer()
                 }
                MonBouton("5") {
                   viewModel.ajouterCaractere("5")
                   viewModel.calculer()
                 }
                MonBouton("6") {
                   viewModel.ajouterCaractere("6")
                   viewModel.calculer()
                 }
                MonBouton("+") {
                   viewModel.ajouterCaractere("+")
                 }
            }

            Row(
                horizontalArrangement = Arrangement.SpaceAround ,
                modifier = Modifier.fillMaxWidth()
                    .padding(end = 8.dp)
            ) {
                Column{

                    MonBouton("1") {
                        viewModel.ajouterCaractere("1")
                        viewModel.calculer()
                    }
                    MonBouton("%") {
                       viewModel.ajouterCaractere("%")
                       viewModel.calculer()
                    }

                }

                Column {
                    MonBouton("2") {
                        viewModel.ajouterCaractere("2")
                        viewModel.calculer()
                    }
                    MonBouton("0") {
                        viewModel.ajouterCaractere("0")
                        viewModel.calculer()
                    }
                }

                Column {
                    MonBouton("3") {
                        viewModel.ajouterCaractere("3")
                        viewModel.calculer()
                    }

                    MonBouton(".") {
                        viewModel.ajouterCaractere(".")
                        viewModel.calculer()
                    }

                }
                Column(
                    modifier = Modifier.fillMaxHeight()
                ) {
                    Button(
                        onClick = {
                            viewModel.calculer()
                            viewModel.egale()
                        },
                        modifier = Modifier.fillMaxHeight(0.8f)
                            .padding(top = 15.dp)
                    ) { Text(
                        "=",
                        fontSize = 35.sp,
                        modifier = Modifier
                            .fillMaxHeight()
                            .padding(vertical = 30.dp)
                    )
                    }
                }
            }


       Spacer(modifier = Modifier.height(15.dp))
        }
    }


}


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun GreetingPreview() {
    MyApplicationTheme {
        Surface {
            Detection()
        }
    }
}
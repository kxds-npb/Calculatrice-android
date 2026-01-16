package com.example.eaetrr

import android.annotation.SuppressLint
import android.health.connect.datatypes.units.Length
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

    class CalculatorViewModel : ViewModel() {

         var chaine by mutableStateOf("")
             private set
        var actuel by mutableStateOf("0")
            private set
        val opsansmoins = listOf("+", "x", "/", "%")
        val opsanspourcentage = listOf("+", "x", "/","-")
        val operateur = listOf("+", "x", "/","-","%")



        fun ajouterCaractere(car: String) {
            //ce if bloque le nombre d'element entrer sans operateur
            val laChaineContientUnOperateurOuAUneLongueurInferieurA14 = chaine.any{
                it.toString() in operateur } || chaine.length < 14 || car in operateur
            if( laChaineContientUnOperateurOuAUneLongueurInferieurA14 ) {
                // aucun operateur au debut sauf moins
                if (!((car in opsansmoins) && chaine.isBlank())) {
                    // deux operateur ne s'affiche pas en mm temps sauf % (ex 5%+2 =2.05)
                    if (!((car in operateur) && (chaine.lastOrNull()?.toString() in opsanspourcentage))) {
                        //on ne divise pas par zero
                        if ((car == "0") && (chaine.lastOrNull()?.toString() == "/")
                        ) {
                            actuel = "Erreur"
                            chaine = ""
                        } else {
                            chaine += car
                        }
                    }
                }
            }
        }

             //gestion du bouton supprimer  "<-"
        fun supprimerValeur() {
                chaine = chaine.dropLast(1)
        }

 //gestion du bouton C
        fun effacer() {
            chaine = ""
            actuel = "0"
        }


        fun calculer() {
                if (!chaine.isBlank()) {
                    //je calcule ssi le dernier element n'est pas un operateur a part "%" ex: (3+4- ne calcule pas)
                    if (chaine.lastOrNull()?.toString() !in opsanspourcentage) {
                        // partie qui verifie si on doit affiché un Int ou un Float
                        if ("." in chaine || "/" in chaine || "%" in chaine) {
                            actuel = evaluerExpression(chaine).toString()
                        } else {
                            actuel = evaluerExpression(chaine).toLong().toString()
                        }
                    }
                }
        }

  //gestion du bouton egale
        fun egale() {
            if("0" != actuel)
                chaine = actuel
        }


        fun evaluerExpression(expression: String): Double {

            // Si l'expression commence par -, ajouter 0 devant
            var expr = expression
            if (expr.startsWith("-")) {
                expr = "0$expr"  // Transforme "-5" en "0-5"
            }
            // Gérer les cas comme "5*-3" -> "5*0-3"
            expr = expr.replace("*-", "*0-")
            expr = expr.replace("/-", "/0-")
            expr = expr.replace("+-", "+0-")
            expr = expr.replace("--", "-0-")

            val tokens = java.util.StringTokenizer(expr, "+-x/%", true)
            val nombres = mutableListOf<Double>()
            val operateurs = mutableListOf<String>()

            while (tokens.hasMoreTokens()) {
                val token = tokens.nextToken().trim()
                if (token.isEmpty()) continue
                if (token in operateur) {
                    operateurs.add(token)
                } else {
                    nombres.add(token.toDouble())
                }
            }

            //Pourcentage
            var i=0
            while(i<operateurs.size){
                when(operateurs[i]){
                    "%" -> {
                        val resultat = nombres[i]/100
                        nombres[i] = resultat
                        operateurs.removeAt(i)
                    }
                    else -> i++
                }
            }


            //Multiplication et Division
            i=0
            while (i<operateurs.size){
                val a = nombres[i]
                val b = nombres[i + 1]

                when(operateurs[i]){
                    "x" -> {
                        val resultat = a * b
                        nombres[i] = resultat
                        nombres.removeAt(i + 1)
                        operateurs.removeAt(i)
                    }
                    "/" -> {
                        if(b != 0.0){
                            val resultat = a / b
                            nombres[i] = resultat
                            nombres.removeAt(i + 1)
                            operateurs.removeAt(i)
                        }else{
                            error("Division par 0")
                        }
                    }
                    else -> i++
                }
            }

            //Addition et Soustraction
            i = 0
            while (i<operateurs.size){
                val a = nombres[i]
                val b = nombres[i + 1]

                when(operateurs[i]){
                    "+" -> {
                        val resultat = a + b
                        nombres[i] = resultat
                        nombres.removeAt(i + 1)
                        operateurs.removeAt(i)
                    }
                    "-" -> {
                        val resultat = a - b
                        nombres[i] = resultat
                        nombres.removeAt(i + 1)
                        operateurs.removeAt(i)
                    }
                    else -> i++
                }
            }
            return nombres[0]
        }
    }



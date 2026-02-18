package com.example.myapplication.ui.theme

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

    class CalculatorViewModel : ViewModel() {

         var chaine by mutableStateOf("")
             private set
        var actuel by mutableStateOf("0")
            private set
        private val opsansmoins = listOf("+", "x", "/", "%")
        private val opsanspourcentage = listOf("+", "x", "/","-")
        private val operateur = listOf("+", "x", "/","-","%")



        fun ajouterCaractere(car: String) {

            if (car == ".") {
                // Sépare la chaîne par les opérateurs pour trouver le dernier nombre
                val dernierNombre = chaine.split(*operateur.toTypedArray()).lastOrNull()
                if (dernierNombre != null && !dernierNombre.contains(".")) {
                    chaine += car
                }
                return // Sortir de la fonction après avoir traité le point
            }

            if (car in operateur && chaine.lastOrNull()?.toString() in operateur && chaine[chaine.length - 2].toString() in operateur) {
                return
            }

            //ce if bloque le nombre d'element entrer sans operateur
            val laChaineContientUnOperateurOuAUneLongueurInferieurA14 = chaine.any{
                it.toString() in operateur } || chaine.length < 14 || car in operateur

            if( laChaineContientUnOperateurOuAUneLongueurInferieurA14 ) {

                // aucun operateur au debut sauf moins
                val caractereNestPasDansOperateurSansMoinsOuChaineNestPasVide =
                    (car !in opsansmoins) || chaine.isNotBlank()

                if (caractereNestPasDansOperateurSansMoinsOuChaineNestPasVide) {

                    // deux operateur ne s'affiche pas en mm temps
                    val caractereNestPasUnOperateurOuDernierElementDeLaChaineEstDifferentDuCaractere =
                        car !in operateur || (chaine.lastOrNull()?.toString() != car)

                    if (caractereNestPasUnOperateurOuDernierElementDeLaChaineEstDifferentDuCaractere) {

                        if (chaine.isNotEmpty() && chaine.last().toString() in opsanspourcentage && car in opsansmoins) {
                            // Si le dernier caractère est un opérateur et que l'utilisateur en tape un autre, on remplace le précédent.
                            chaine = chaine.dropLast(1) + car
                        } else {
                            //on ne divise pas par zero
                            val caractereEstZeroEtDernierElementDeLaChaineEstUnDiviseur =
                                (car == "0") && (chaine.lastOrNull()?.toString() == "/")

                            if (caractereEstZeroEtDernierElementDeLaChaineEstUnDiviseur) {
                                actuel = "Erreur"
                                chaine = ""
                            } else {
                                chaine += car
                            }
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
                if (chaine.isNotBlank()) {
                    //je calcule ssi le dernier element n'est pas un operateur a part "%" ex: (3+4- ne calcule pas)
                    if (chaine.lastOrNull()?.toString() !in opsanspourcentage) {
                        // partie qui verifie si on doit affiché un Int ou un Float
                        actuel = if ("." in chaine || "/" in chaine || "%" in chaine) {
                            evaluerExpression(chaine).toString()
                        } else {
                            evaluerExpression(chaine).toLong().toString()
                        }
                    }
                }
        }

  //gestion du bouton egale
        fun egale() {
            if("0" != actuel)
                chaine = actuel
        }


        private fun evaluerExpression(expression: String): Double {
            var expr = expression

            // Nettoyer l'expression en enlevant un éventuel opérateur à la fin
            if (expr.lastOrNull()?.toString() in opsanspourcentage) {
                expr = expr.dropLast(1)
            }

            // Si l'expression commence par -, ajouter 0 devant
            if (expr.startsWith("-")) {
                expr = "0$expr"  // Transforme "-5" en "0-5"
            }
            // Gérer les cas comme "5*-3" -> "5*0-3"
            expr = expr.replace("x-", "x1-")
            expr = expr.replace("/-", "/1-")
            expr = expr.replace("+-", "-")

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

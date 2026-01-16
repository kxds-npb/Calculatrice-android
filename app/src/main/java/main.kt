fun main() {

    val maliste =  listOf(1,4,5,12,7,8)
    val listepair = maliste.filter { it%2==0 }
    val listedouble = maliste.map { it *2 }
    for (lis in listepair){
        print(lis)
    }
    println("")
    for (doub in listedouble){
        print("$doub\t")
    }
}

/*Programme qui prend un
e liste en entrée et stocke les elements pair
de cette liste dans une autre liste

*/

/* Programme recupere deux valeur, fais leur somme et affiche le resultat dans un textfield */

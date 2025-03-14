package fr.isen.animalpark.models

data class User (
    val uid: String,
    val isAdmin: Boolean = false
){
    companion object{
        private var currentUser: User? = null

        fun setCurrentUser(user: User){
            currentUser = user
        }

        fun getCurrentUser(): User? {
            return currentUser
        }
    }
}
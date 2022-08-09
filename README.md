# PikaPoke
The project uses a set of Android Jetpack libraries to display data from [https://pokeapi.co/] REST API.
The app uses Kotlin.

## Architecture
The project uses MVVM architecture pattern.

## Libraries
* [ViewModel] - Manage UI related data in a lifecycle conscious way and act as a channel between use cases and ui.
* [Navigation Component] - Android Jetpack's Navigation component helps in implementing navigation between fragments.
* [Dagger Hilt] - For dependency injection.
* [Paging 3] - Allow pagination of the Data.
* [Retrofit + Okhttp3] - To access the Rest Api.
* [Kotlin Flow] - To access data sequentially.
* [Datastore] - To store data in key value pairs.
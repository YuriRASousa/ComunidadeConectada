package com.fiap.comunidadeconectada

import android.app.Application
import com.fiap.comunidadeconectada.api.RetrofitClient
import com.fiap.comunidadeconectada.data.AppDatabase
import com.fiap.comunidadeconectada.data.PreferencesManager
import com.fiap.comunidadeconectada.data.Repository

import com.fiap.comunidadeconectada.data.entities.ResourceEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ComunidadeConectadaApplication : Application() {

    // Dependências instanciadas de forma manual (Service Locator simplificado)
    val database by lazy { AppDatabase.getDatabase(this) }
    val preferencesManager by lazy { PreferencesManager(this) }
    
    val repository by lazy {
        Repository(
            apiService = RetrofitClient.apiService,
            preferencesManager = preferencesManager,
            userDao = database.userDao(),
            resourceDao = database.resourceDao(),
            messageDao = database.messageDao(),
            ratingDao = database.ratingDao(),
            transactionDao = database.transactionDao(),
            eventDao = database.eventDao(),
            postDao = database.postDao()
        )
    }

    override fun onCreate() {
        super.onCreate()
        seedDatabase()
    }

    private fun seedDatabase() {
        CoroutineScope(Dispatchers.IO).launch {
            val resourceDao = database.resourceDao()
            val userDao = database.userDao()
            
            // 1. Criar usuários reais para os anúncios
            val fakeUsers = listOf(
                com.fiap.comunidadeconectada.data.entities.UserEntity(
                    id = "user_1", name = "João Silva", email = "joao@email.com", address = "Rua A, 10", reputation = 4.8f
                ),
                com.fiap.comunidadeconectada.data.entities.UserEntity(
                    id = "user_2", name = "Maria Santos", email = "maria@email.com", address = "Rua B, 20", reputation = 4.9f
                ),
                com.fiap.comunidadeconectada.data.entities.UserEntity(
                    id = "user_3", name = "Ricardo Oliveira", email = "ricardo@email.com", address = "Rua C, 30", reputation = 4.5f
                ),
                com.fiap.comunidadeconectada.data.entities.UserEntity(
                    id = "user_4", name = "Ana Costa", email = "ana@email.com", address = "Rua D, 40", reputation = 4.7f
                ),
                com.fiap.comunidadeconectada.data.entities.UserEntity(
                    id = "user_5", name = "Carlos Souza", email = "carlos@email.com", address = "Rua E, 50", reputation = 5.0f
                )
            )
            fakeUsers.forEach { userDao.insert(it) }

            // 2. Criar anúncios diversificados e vinculados
            val existing = resourceDao.getAllActiveResourcesList()
            if (existing.size <= 1) {
                val fakeResources = listOf(
                    ResourceEntity(
                        id = "1",
                        title = "Furadeira de Impacto Bosch",
                        description = "Furadeira profissional 750W, acompanha jogo de brocas para concreto e madeira. Perfeita para instalações rápidas.",
                        category = "Ferramentas",
                        condition = "Excelente",
                        offerantId = "user_1",
                        availability = "Segunda a Sexta",
                        imageUrl = "https://images.unsplash.com/photo-1504148455328-c376907d081c?q=80&w=500",
                        type = "Empréstimo"
                    ),
                    ResourceEntity(
                        id = "2",
                        title = "Cadeira de Rodas Ortobrás",
                        description = "Cadeira de rodas manual, modelo dobrável em X. Estrutura em alumínio, muito leve. Para quem precisa de mobilidade temporária.",
                        category = "Saúde",
                        condition = "Muito Bom",
                        offerantId = "user_2",
                        availability = "Disponível Imediatamente",
                        imageUrl = "https://images.unsplash.com/photo-1576765608535-5f04d1e3f289?q=80&w=500",
                        type = "Doação"
                    ),
                    ResourceEntity(
                        id = "3",
                        title = "Livros de Cálculo (Stewart)",
                        description = "Volumes 1 e 2 da coleção James Stewart. 7ª Edição. Ótimo estado, sem rasuras. Empresto para o semestre letivo.",
                        category = "Educação",
                        condition = "Usado",
                        offerantId = "user_3",
                        availability = "Final de Semana",
                        imageUrl = "https://images.unsplash.com/photo-1543004218-294b15097a1a?q=80&w=500",
                        type = "Empréstimo"
                    ),
                    ResourceEntity(
                        id = "4",
                        title = "Escada de Alumínio 7m",
                        description = "Escada extensível e articulada. Alcança até 7 metros. Ideal para limpeza de calhas ou pintura de fachadas.",
                        category = "Ferramentas",
                        condition = "Bom",
                        offerantId = "user_4",
                        availability = "A combinar",
                        imageUrl = "https://images.unsplash.com/photo-1581092160562-40aa08e78837?q=80&w=500",
                        type = "Empréstimo"
                    ),
                    ResourceEntity(
                        id = "5",
                        title = "Aulas de Violão para Iniciantes",
                        description = "Sou músico há 10 anos e ofereço aulas básicas de violão popular. Procuro em troca alguém que possa ajudar com jardinagem.",
                        category = "Serviços",
                        condition = "N/A",
                        offerantId = "user_5",
                        availability = "Terças e Quintas",
                        imageUrl = "https://images.unsplash.com/photo-1510915361894-db8b60106cb1?q=80&w=500",
                        type = "Troca"
                    )
                )
                fakeResources.forEach { resourceDao.insert(it) }
            }
        }
    }
}

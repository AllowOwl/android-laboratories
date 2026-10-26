package com.example.recipebook.ui.list

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.recipebook.R
import com.example.recipebook.data.model.Category
import com.example.recipebook.databinding.ActivityMainBinding
import com.example.recipebook.ui.detail.RecipeDetailActivity
import com.example.recipebook.ui.editor.EditRecipeActivity
import com.example.recipebook.ui.settings.SettingsActivity
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: RecipeListViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val adapter = RecipeAdapter { recipeId ->
            val intent = Intent(this, RecipeDetailActivity::class.java)
            intent.putExtra(RecipeDetailActivity.EXTRA_RECIPE_ID, recipeId)
            startActivity(intent)
        }
        binding.recipeList.adapter = adapter

        binding.searchInput.doAfterTextChanged { text ->
            viewModel.setQuery(text.toString())
        }

        binding.filterChips.setOnCheckedStateChangeListener { _, checkedIds ->
            when (checkedIds.firstOrNull()) {
                R.id.chipFavorites -> viewModel.showFavorites()
                R.id.chipSoup -> viewModel.showCategory(Category.SOUP)
                R.id.chipMainDish -> viewModel.showCategory(Category.MAIN_DISH)
                R.id.chipSideDish -> viewModel.showCategory(Category.SIDE_DISH)
                R.id.chipSalad -> viewModel.showCategory(Category.SALAD)
                R.id.chipSnack -> viewModel.showCategory(Category.SNACK)
                R.id.chipDessert -> viewModel.showCategory(Category.DESSERT)
                R.id.chipDrink -> viewModel.showCategory(Category.DRINK)
                else -> viewModel.showAll()
            }
        }

        binding.addRecipeButton.setOnClickListener {
            startActivity(Intent(this, EditRecipeActivity::class.java))
        }

        binding.toolbar.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.actionSettings -> {
                    startActivity(Intent(this, SettingsActivity::class.java))
                    true
                }
                else -> false
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.recipes.collect { recipes ->
                    adapter.submitList(recipes)
                    binding.emptyState.isVisible = recipes.isEmpty()
                }
            }
        }
    }
}
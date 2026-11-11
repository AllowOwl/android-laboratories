package com.example.recipebook.ui.detail

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.recipebook.R
import com.example.recipebook.data.model.Recipe
import com.example.recipebook.databinding.ActivityRecipeDetailBinding
import com.example.recipebook.ui.appearance
import com.example.recipebook.ui.editor.EditRecipeActivity
import com.example.recipebook.ui.formatNumber
import com.example.recipebook.ui.label
import kotlinx.coroutines.launch

class RecipeDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRecipeDetailBinding
    private val viewModel: RecipeDetailViewModel by viewModels()

    private var recipe: Recipe? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityRecipeDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val ingredientAdapter = IngredientAdapter()
        binding.ingredientList.adapter = ingredientAdapter

        binding.toolbar.setNavigationOnClickListener {
            finish()
        }

        binding.toolbar.setOnMenuItemClickListener { item ->
            val current = recipe
            when (item.itemId) {
                R.id.actionFavorite -> {
                    if (current != null) {
                        viewModel.setFavorite(!current.isFavorite)
                    }
                    true
                }
                R.id.actionEdit -> {
                    if (current != null) {
                        val intent = Intent(this, EditRecipeActivity::class.java)
                        intent.putExtra(EditRecipeActivity.EXTRA_RECIPE_ID, current.id)
                        startActivity(intent)
                    }
                    true
                }
                else -> false
            }
        }

        binding.recalculateButton.setOnClickListener {
            val current = recipe ?: return@setOnClickListener
            val wanted = binding.yieldInput.text.toString().replace(',', '.').toDoubleOrNull()
            if (wanted == null || wanted <= 0) {
                binding.yieldLayout.error = getString(R.string.error_number)
            } else {
                binding.yieldLayout.error = null
                viewModel.scaleTo(wanted, current.yieldAmount)
                WindowCompat.getInsetsController(window, binding.root)
                    .hide(WindowInsetsCompat.Type.ime())
            }
        }

        binding.resetButton.setOnClickListener {
            val current = recipe ?: return@setOnClickListener
            binding.yieldLayout.error = null
            binding.yieldInput.setText(formatNumber(current.yieldAmount.toDouble()))
            viewModel.resetScale()
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.recipe.collect { loaded ->
                        if (loaded == null) {
                            finish()
                        } else {
                            recipe = loaded
                            showRecipe(loaded)
                        }
                    }
                }
                launch {
                    viewModel.lines.collect { lines ->
                        ingredientAdapter.submitList(lines)
                    }
                }
                launch {
                    viewModel.factor.collect { k ->
                        binding.ingredientsHeader.text = if (k == 1.0) {
                            getString(R.string.section_ingredients)
                        } else {
                            getString(R.string.section_ingredients_scaled, formatNumber(k))
                        }
                    }
                }
            }
        }
    }

    private fun showRecipe(recipe: Recipe) {
        val appearance = recipe.category.appearance()
        val yieldUnit = getString(recipe.yieldUnit.label())

        binding.recipeTitle.text = recipe.title
        binding.categoryText.setText(appearance.title)
        binding.categoryIcon.setImageResource(appearance.icon)
        binding.categoryIcon.backgroundTintList =
            ContextCompat.getColorStateList(this, appearance.container)
        binding.categoryIcon.imageTintList =
            ContextCompat.getColorStateList(this, appearance.onContainer)
        binding.cookingTimeText.text = getString(R.string.format_minutes, recipe.cookingTimeMinutes)

        binding.yieldLayout.suffixText = yieldUnit
        binding.yieldLayout.helperText =
            getString(R.string.helper_yield_original, "${recipe.yieldAmount} $yieldUnit")
        if (binding.yieldInput.text.isNullOrEmpty()) {
            binding.yieldInput.setText(formatNumber(recipe.yieldAmount.toDouble()))
        }

        binding.stepsText.text = recipe.steps.lines()
            .filter { it.isNotBlank() }
            .mapIndexed { index, step -> "${index + 1}. ${step.trim()}" }
            .joinToString("\n")

        val favoriteItem = binding.toolbar.menu.findItem(R.id.actionFavorite)
        if (recipe.isFavorite) {
            favoriteItem.setIcon(R.drawable.ic_star)
            favoriteItem.setTitle(R.string.action_unfavorite)
        } else {
            favoriteItem.setIcon(R.drawable.ic_star_border)
            favoriteItem.setTitle(R.string.action_favorite)
        }
    }

    companion object {
        const val EXTRA_RECIPE_ID = "recipe_id"
    }
}

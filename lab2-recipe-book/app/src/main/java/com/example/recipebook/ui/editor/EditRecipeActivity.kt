package com.example.recipebook.ui.editor

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.recipebook.R
import com.example.recipebook.data.model.Category
import com.example.recipebook.data.model.Recipe
import com.example.recipebook.data.model.YieldUnit
import com.example.recipebook.databinding.ActivityEditRecipeBinding
import com.example.recipebook.ui.formatNumber
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.launch

class EditRecipeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditRecipeBinding
    private val viewModel: EditRecipeViewModel by viewModels()
    private lateinit var ingredientAdapter: EditIngredientAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityEditRecipeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val bars = insets.getInsets(
                WindowInsetsCompat.Type.systemBars() or WindowInsetsCompat.Type.ime()
            )
            v.setPadding(bars.left, bars.top, bars.right, bars.bottom)
            insets
        }

        if (!viewModel.isNew) {
            binding.toolbar.setTitle(R.string.title_edit_recipe)
            binding.deleteButton.isVisible = true
        }

        ingredientAdapter = EditIngredientAdapter(viewModel.lines)
        binding.ingredientList.adapter = ingredientAdapter
        binding.ingredientList.itemAnimator = null

        binding.addIngredientButton.setOnClickListener {
            ingredientAdapter.addLine()
        }
        binding.categoryInput.setOnItemClickListener { _, _, position, _ ->
            viewModel.category = Category.entries[position]
        }
        binding.toolbar.setNavigationOnClickListener {
            finish()
        }
        binding.toolbar.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.actionSave -> {
                    saveRecipe()
                    true
                }
                else -> false
            }
        }
        binding.deleteButton.setOnClickListener {
            confirmDelete()
        }

        if (!viewModel.isLoaded) {
            lifecycleScope.launch {
                val recipe = viewModel.load()
                if (recipe != null) {
                    fillForm(recipe)
                }
                ingredientAdapter.notifyItemRangeInserted(0, viewModel.lines.size)
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.ingredientNames.collect { names ->
                    ingredientAdapter.setSuggestions(names)
                }
            }
        }
    }

    private fun fillForm(recipe: Recipe) {
        val categoryNames = resources.getStringArray(R.array.categories)
        binding.titleInput.setText(recipe.title)
        binding.categoryInput.setText(categoryNames[recipe.category.ordinal], false)
        binding.cookingTimeInput.setText(formatNumber(recipe.cookingTimeMinutes.toDouble()))
        binding.yieldInput.setText(formatNumber(recipe.yieldAmount.toDouble()))
        if (recipe.yieldUnit == YieldUnit.MILLILITER) {
            binding.yieldUnitToggle.check(R.id.milliliterButton)
        } else {
            binding.yieldUnitToggle.check(R.id.gramButton)
        }
        binding.stepsInput.setText(recipe.steps)
    }

    private fun saveRecipe() {
        val title = binding.titleInput.text.toString().trim()
        val category = viewModel.category
        val cookingTime = binding.cookingTimeInput.text.toString().toIntOrNull()?.takeIf { it > 0 }
        val yieldAmount = binding.yieldInput.text.toString().toIntOrNull()?.takeIf { it > 0 }

        binding.titleLayout.error = if (title.isEmpty()) getString(R.string.error_name_required) else null
        binding.categoryLayout.error = if (category == null) getString(R.string.error_category) else null
        binding.cookingTimeLayout.error = if (cookingTime == null) getString(R.string.error_number) else null
        binding.yieldLayout.error = if (yieldAmount == null) getString(R.string.error_number) else null
        val linesOk = viewModel.checkLines()
        ingredientAdapter.notifyItemRangeChanged(0, ingredientAdapter.itemCount)

        if (title.isEmpty() || category == null || cookingTime == null || yieldAmount == null || !linesOk) {
            return
        }

        val yieldUnit = if (binding.yieldUnitToggle.checkedButtonId == R.id.milliliterButton) {
            YieldUnit.MILLILITER
        } else {
            YieldUnit.GRAM
        }
        val steps = binding.stepsInput.text.toString().trim()

        lifecycleScope.launch {
            viewModel.save(title, category, cookingTime, yieldAmount, yieldUnit, steps)
            finish()
        }
    }

    private fun confirmDelete() {
        MaterialAlertDialogBuilder(this)
            .setTitle(R.string.dialog_delete_title)
            .setMessage(R.string.dialog_delete_message)
            .setNegativeButton(R.string.action_cancel, null)
            .setPositiveButton(R.string.action_delete) { _, _ ->
                lifecycleScope.launch {
                    viewModel.delete()
                    finish()
                }
            }
            .show()
    }

    companion object {
        const val EXTRA_RECIPE_ID = "recipe_id"
    }
}

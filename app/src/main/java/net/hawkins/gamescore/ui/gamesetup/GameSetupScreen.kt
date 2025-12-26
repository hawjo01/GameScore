package net.hawkins.gamescore.ui.gamesetup

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.shapes
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import net.hawkins.gamescore.R
import net.hawkins.gamescore.data.model.Game
import net.hawkins.gamescore.ui.theme.Typography

@Composable
fun GameSetupScreen(
    viewModel: GameSetupViewModel,
    onCancel: () -> Unit,
    onModifyGame: () -> Unit,
    onSaveNewGame: () -> Unit,
    modifier: Modifier = Modifier
) {
    LaunchedEffect(Unit) {
        viewModel.updateTopAppBar { /* No AppBarActions */ }
    }

    val uiState by viewModel.uiState.collectAsState()
    GameSetupScreenContent(
        uiState = uiState,
        onNameChange = { newName -> viewModel.setGameName(newName) },
        onConstraintAllowNegativeChange = { newState ->
            viewModel.setConstraintAllowNegative(
                newState
            )
        },
        onConstraintEqualHandSizesChange = { newState ->
            viewModel.setConstraintEqualHandSizes(
                newState
            )
        },
        onGoalChange = { newGoal -> viewModel.setObjectiveGoal(newGoal) },
        onObjectiveTypeChange = { newType -> viewModel.setObjectiveType(newType) },
        onModulusChange = { newModulus -> viewModel.setConstraintModulus(newModulus) },
        onDisplayNegativeChange = { newValue -> viewModel.setDisplayNegative(newValue) },
        onDisplayPositiveChange = { newValue -> viewModel.setDisplayPositive(newValue) },
        onSaveGame = onSaveNewGame,
        onEnableEdits = { viewModel.setMode(GameSetupUiState.Mode.EDIT) },
        onModifyGame = onModifyGame,
        onCancel = onCancel,
        modifier = modifier
    )
}

@Composable
private fun GameSetupScreenContent(
    uiState: GameSetupUiState,
    onNameChange: (String) -> Unit,
    onConstraintAllowNegativeChange: (Boolean) -> Unit,
    onConstraintEqualHandSizesChange: (Boolean) -> Unit,
    onObjectiveTypeChange: (Game.Objective.Type) -> Unit,
    onGoalChange: (Int?) -> Unit,
    onModulusChange: (Int?) -> Unit,
    onDisplayPositiveChange: (Game.Colors.Color) -> Unit,
    onDisplayNegativeChange: (Game.Colors.Color) -> Unit,
    onSaveGame: () -> Unit,
    onEnableEdits: () -> Unit,
    onModifyGame: () -> Unit,
    onCancel: () -> Unit,
    modifier: Modifier
) {
    val scrollState = rememberScrollState()
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxWidth()
            .verticalScroll(scrollState)
            .padding(horizontal = 10.dp)
    ) {
        if (uiState.mode == GameSetupUiState.Mode.NEW) {
            Text(stringResource(R.string.new_game), style = Typography.headlineLarge)
        }
        NameCard(
            uiState = uiState,
            onNameChange = onNameChange,
            modifier = modifier
        )
        ObjectiveCard(
            uiState,
            onObjectiveTypeChange = onObjectiveTypeChange,
            onGoalChange = onGoalChange,
            modifier = modifier
        )
        ConstraintCard(
            uiState = uiState,
            onConstraintAllowNegativeChange = onConstraintAllowNegativeChange,
            onModulusChange = onModulusChange,
            onConstraintEqualHandSizesChange = onConstraintEqualHandSizesChange,
            modifier = modifier
        )
        DisplayColorsCard(
            uiState = uiState,
            onDisplayNegativeChange = onDisplayNegativeChange,
            onDisplayPositiveChange = onDisplayPositiveChange,
            modifier = modifier
        )
        ActionButtons(
            uiState = uiState,
            onSaveGame = onSaveGame,
            onCancel = onCancel,
            onEnableEdits = onEnableEdits,
            onModifyGame = onModifyGame,
            modifier = modifier
        )
    }
}

@Composable
private fun ActionButtons(
    uiState: GameSetupUiState,
    onSaveGame: () -> Unit,
    onCancel: () -> Unit,
    onEnableEdits: () -> Unit,
    onModifyGame: () -> Unit,
    modifier: Modifier
) {
    GameSectionRow(
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        modifier = modifier
    ) {
        OutlinedButton(
            onClick = onCancel,
        ) {
            Text(stringResource(R.string.cancel))
        }
        when (uiState.mode) {
            GameSetupUiState.Mode.NEW -> OutlinedButton(
                onClick = onSaveGame,
            ) {
                Text(stringResource(R.string.save))
            }

            GameSetupUiState.Mode.EDIT -> OutlinedButton(
                onClick = onModifyGame
            ) {
                Text(stringResource(R.string.modify))
            }

            else -> OutlinedButton(
                onClick = onEnableEdits
            ) {
                Text(stringResource(R.string.change))
            }
        }
    }
}

@Composable
private fun NameCard(
    uiState: GameSetupUiState,
    onNameChange: (String) -> Unit,
    modifier: Modifier
) {
    GameSectionCard(
        modifier = modifier
    ) {
        GameSectionRow(
            modifier = modifier
        )
        {
            val keyboardController = LocalSoftwareKeyboardController.current
            val hideKeyboard = { keyboardController?.hide() }

            OutlinedTextField(
                value = uiState.game.name,
                onValueChange = { newName -> onNameChange(newName) },
                label = { Text(text = stringResource(R.string.name)) },
                singleLine = true,
                shape = shapes.small,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Ascii,
                    capitalization = KeyboardCapitalization.Words,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        hideKeyboard.invoke()
                    }),
                readOnly = uiState.mode == GameSetupUiState.Mode.VIEW,
                modifier = modifier
            )
        }
    }
}

@Composable
private fun DisplayColorsCard(
    uiState: GameSetupUiState,
    onDisplayNegativeChange: (Game.Colors.Color) -> Unit,
    onDisplayPositiveChange: (Game.Colors.Color) -> Unit,
    modifier: Modifier
) {
    GameSectionCard(
        title = stringResource(R.string.display_colors),
        modifier = modifier
    )
    {
        DisplayTypeDropMenu(
            label = stringResource(R.string.positive_score),
            value = uiState.game.color.positiveScore,
            onChange = onDisplayPositiveChange,
            readOnly = uiState.mode == GameSetupUiState.Mode.VIEW,
            modifier = modifier
        )
        DisplayTypeDropMenu(
            label = stringResource(R.string.negative_score),
            value = uiState.game.color.negativeScore,
            onChange = onDisplayNegativeChange,
            readOnly = uiState.mode == GameSetupUiState.Mode.VIEW,
            modifier = modifier
        )
    }
}

@Composable
private fun ConstraintCard(
    uiState: GameSetupUiState,
    onConstraintAllowNegativeChange: (Boolean) -> Unit,
    onConstraintEqualHandSizesChange: (Boolean) -> Unit,
    onModulusChange: (Int?) -> Unit,
    modifier: Modifier
) {
    GameSectionCard(
        title = stringResource(R.string.score_constraints),
        modifier = modifier
    ) {
        SwitchWithLabel(
            label = stringResource(R.string.only_positive),
            initialChecked = uiState.game.constraints.positiveOnly,
            onCheckedChange = { newCheckedState ->
                onConstraintAllowNegativeChange(newCheckedState)
            },
            readOnly = uiState.mode == GameSetupUiState.Mode.VIEW,
            modifier = modifier
        )
        SwitchWithLabel(
            label = "Require Equal Hand Sizes",
            initialChecked = uiState.game.constraints.equalHandSizes,
            onCheckedChange = { newCheckedState ->
                onConstraintEqualHandSizesChange(newCheckedState)
            },
            readOnly = uiState.mode == GameSetupUiState.Mode.VIEW,
            modifier = modifier
        )
        NullableIntOutlinedTextField(
            label = stringResource(R.string.multiple_of),
            number = uiState.game.constraints.multipleOf,
            onValueChange = onModulusChange,
            readOnly = uiState.mode == GameSetupUiState.Mode.VIEW,
            modifier = modifier
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun GameSectionCard(
    title: String = "",
    modifier: Modifier,
    content: @Composable () -> Unit
) {

    var expanded by remember { mutableStateOf(false) }
    val rotationState by animateFloatAsState(
        targetValue = if (expanded) 180f else 0f,
        label = "arrowRotation"
    )
    Card(
        modifier = modifier
            .padding(top = 10.dp)
            .animateContentSize()
    ) {
        if (title.isNotBlank()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { expanded = !expanded } // Toggle expansion on click
                    .padding(all = 5.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = title, style = Typography.titleSmall)
                Icon(
                    imageVector = Icons.Default.KeyboardArrowDown,
                    contentDescription = "Expand/Collapse Arrow",
                    modifier = Modifier.rotate(rotationState) // Rotate the arrow icon
                )
            }
            AnimatedVisibility(
                visible = expanded,
                enter = expandVertically(expandFrom = Alignment.Top),
                exit = shrinkVertically(shrinkTowards = Alignment.Top)
            ) {
                Column {
                    content()
                }
            }
        } else {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                content()
            }
        }
    }
}

@Composable
private fun GameSectionRow(
    modifier: Modifier,
    verticalAlignment: Alignment.Vertical = Alignment.Top,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.Start,
    content: @Composable RowScope.() -> Unit,
) {
    Row(
        verticalAlignment = verticalAlignment,
        horizontalArrangement = horizontalArrangement,
        modifier = modifier.padding(all = 5.dp)
    ) {
        content()
    }
}

@Composable
private fun ObjectiveCard(
    uiState: GameSetupUiState,
    onObjectiveTypeChange: (Game.Objective.Type) -> Unit,
    onGoalChange: (Int?) -> Unit,
    modifier: Modifier
) {
    GameSectionCard(
        title = stringResource(R.string.objective),
        modifier = modifier
    ) {
        ObjectiveTypeDropMenu(
            uiState.game.objective.type,
            onObjectiveTypeChange = onObjectiveTypeChange,
            readOnly = uiState.mode == GameSetupUiState.Mode.VIEW,
            modifier = modifier
        )
        NullableIntOutlinedTextField(
            label = "Goal",
            number = uiState.game.objective.goal,
            onValueChange = onGoalChange,
            readOnly = uiState.mode == GameSetupUiState.Mode.VIEW,
            modifier = modifier
        )
    }
}

@Composable
private fun NullableIntOutlinedTextField(
    label: String,
    number: Int?,
    onValueChange: (Int?) -> Unit,
    readOnly: Boolean,
    modifier: Modifier
) {
    GameSectionRow(
        modifier = modifier
    )
    {
        val keyboardController = LocalSoftwareKeyboardController.current
        val hideKeyboard = { keyboardController?.hide() }

        val value = number?.toString() ?: ""
        OutlinedTextField(
            value = value,
            onValueChange = { newValue ->
                // Only update the state if the new value consists only of digits
                if (newValue.all { it.isDigit() }) {
                    onValueChange(newValue.toIntOrNull())
                }
            },
            label = { Text(text = label) },
            singleLine = true,
            shape = shapes.small,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    hideKeyboard.invoke()
                }),
            readOnly = readOnly,
            modifier = modifier
        )
    }
}

@Composable
private fun SwitchWithLabel(
    label: String,
    initialChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    readOnly: Boolean,
    modifier: Modifier
) {
    GameSectionRow(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        var checkedState by remember { mutableStateOf(initialChecked) }

        Switch(
            checked = checkedState,
            onCheckedChange = {
                if (readOnly) {
                    null
                } else {
                    checkedState = it
                    onCheckedChange(it)
                }
            },
            enabled = !readOnly
        )
        Text(
            text = label,
            modifier = modifier
                .padding(start = 10.dp)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ObjectiveTypeDropMenu(
    value: Game.Objective.Type,
    onObjectiveTypeChange: (Game.Objective.Type) -> Unit,
    readOnly: Boolean,
    modifier: Modifier
) {
    GameSectionRow(
        modifier = modifier
    )
    {
        var expanded by remember { mutableStateOf(false) }
        val selectedText = toDisplayName(value)

        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = {
                if (!readOnly) {
                    expanded = !expanded
                }
            }
        ) {
            OutlinedTextField(
                value = selectedText,
                onValueChange = {},
                readOnly = true,
                label = { Text(text = stringResource(R.string.type)) },
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                },
                modifier = Modifier
                    .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable)
                    .padding(bottom = 5.dp)
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                Game.Objective.Type.entries.forEach { selectionOption ->
                    DropdownMenuItem(
                        text = { Text(toDisplayName(selectionOption)) },
                        onClick = {
                            onObjectiveTypeChange(selectionOption)
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun toDisplayName(type: Game.Objective.Type): String {
    return when (type) {
        Game.Objective.Type.HIGH_SCORE -> stringResource(R.string.high_score)
        Game.Objective.Type.LOW_SCORE -> stringResource(R.string.low_score)
    }
}

@Composable
private fun toDisplayName(color: Game.Colors.Color): String {
    return when (color) {
        Game.Colors.Color.DEFAULT -> stringResource(R.string.normal)
        Game.Colors.Color.RED -> stringResource(R.string.red)
        Game.Colors.Color.GREEN -> stringResource(R.string.green)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DisplayTypeDropMenu(
    label: String,
    value: Game.Colors.Color,
    onChange: (Game.Colors.Color) -> Unit,
    readOnly: Boolean,
    modifier: Modifier
) {
    GameSectionRow(
        modifier = modifier
    )
    {
        var expanded by remember { mutableStateOf(false) }
        val selectedText = toDisplayName(value)

        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = {
                if (!readOnly) {
                    expanded = !expanded
                }
            }
        ) {
            OutlinedTextField(
                value = selectedText,
                onValueChange = {},
                readOnly = true,
                label = { Text(text = label) },
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                },
                modifier = Modifier
                    .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable)
                    .padding(bottom = 5.dp)
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                Game.Colors.Color.entries.forEach { selectionOption ->
                    DropdownMenuItem(
                        text = { Text(toDisplayName(selectionOption)) },
                        onClick = {
                            onChange(selectionOption)
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun GameSetupContentPreview() {
    GameSetupScreenContent(
        uiState = GameSetupUiState(
            Game(
                name = "2500",
                objective = Game.Objective(
                    goal = 2500
                ),
                constraints = Game.Constraints(
                    multipleOf = 5
                ),
                color = Game.Colors(
                    negativeScore = Game.Colors.Color.RED
                )
            )
        ),
        onConstraintAllowNegativeChange = { _ -> },
        onConstraintEqualHandSizesChange = { _ -> },
        onNameChange = { _ -> },
        onObjectiveTypeChange = { _ -> },
        onGoalChange = { _ -> },
        onModulusChange = { _ -> },
        onDisplayNegativeChange = { _ -> },
        onDisplayPositiveChange = { _ -> },
        onSaveGame = {},
        onEnableEdits = {},
        onModifyGame = {},
        onCancel = {},
        modifier = Modifier
    )
}
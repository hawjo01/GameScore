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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme.shapes
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.graphics.Color
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
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.updateTopAppBar(
            newNavigationIcon = {
                NavigationIcon(onBack = onCancel)
            },
            newActions = {
                AppBarActions(
                    screenMode = uiState.mode,
                    onEvent = { event: GameSetupUiEvent -> viewModel.onEvent(event) },
                    onSaveNewGame = onSaveNewGame,
                    onModifyGame = onModifyGame,
                )
            }
        )
    }

    GameSetupScreenContent(
        uiState = uiState,
        onEvent = { event: GameSetupUiEvent -> viewModel.onEvent(event) },
        modifier = modifier
    )
}

@Composable
private fun AppBarActions(
    screenMode: GameSetupUiState.Mode,
    onEvent: (GameSetupUiEvent) -> Unit,
    onModifyGame: () -> Unit,
    onSaveNewGame: () -> Unit,
) {
    val actionText: String
    val action: () -> Unit
    when (screenMode) {
        GameSetupUiState.Mode.NEW -> {
            actionText = stringResource(R.string.save)
            action = onSaveNewGame
        }

        GameSetupUiState.Mode.EDIT -> {
            actionText = stringResource(R.string.apply)
            action = onModifyGame
        }

        GameSetupUiState.Mode.VIEW -> {
            actionText = stringResource(R.string.edit)
            action = { onEvent(GameSetupUiEvent.SetScreenMode(GameSetupUiState.Mode.EDIT)) }
        }
    }

    TextButton(
        onClick = action,
        colors = ButtonDefaults.textButtonColors(
            contentColor = Color.Blue
        )
    ) {
        Text(actionText)
    }
}

@Composable
private fun GameSetupScreenContent(
    uiState: GameSetupUiState,
    onEvent: (GameSetupUiEvent) -> Unit,
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
            onEvent = onEvent,
            modifier = modifier
        )
        ObjectiveCard(
            uiState,
            onEvent = onEvent,
            modifier = modifier
        )
        ConstraintCard(
            uiState = uiState,
            onEvent = onEvent,
            modifier = modifier
        )
        DisplayColorsCard(
            uiState = uiState,
            onEvent = onEvent,
            modifier = modifier
        )
    }
}

@Composable
private fun NameCard(
    uiState: GameSetupUiState,
    onEvent: (GameSetupUiEvent) -> Unit,
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
                onValueChange = { newName -> onEvent(GameSetupUiEvent.SetGameName(newName)) },
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
    onEvent: (GameSetupUiEvent) -> Unit,
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
            onChange = { color: Game.Colors.Color ->
                onEvent(
                    GameSetupUiEvent.SetDisplayPositiveColor(
                        color
                    )
                )
            },
            readOnly = uiState.mode == GameSetupUiState.Mode.VIEW,
            modifier = modifier
        )
        DisplayTypeDropMenu(
            label = stringResource(R.string.negative_score),
            value = uiState.game.color.negativeScore,
            onChange = { color: Game.Colors.Color ->
                onEvent(
                    GameSetupUiEvent.SetDisplayNegativeColor(
                        color
                    )
                )
            },
            readOnly = uiState.mode == GameSetupUiState.Mode.VIEW,
            modifier = modifier
        )
    }
}

@Composable
private fun ConstraintCard(
    uiState: GameSetupUiState,
    onEvent: (GameSetupUiEvent) -> Unit,
    modifier: Modifier
) {
    GameSectionCard(
        title = stringResource(R.string.score_constraints),
        modifier = modifier
    ) {
        SwitchWithLabel(
            label = stringResource(R.string.only_positive),
            initialState = uiState.game.constraints.positiveOnly,
            onCheckedChange =
                { newCheckedState ->
                    onEvent(GameSetupUiEvent.SetConstraintPositiveOnlyScores(newCheckedState))
                },
            readOnly = uiState.mode == GameSetupUiState.Mode.VIEW,
            modifier = modifier
        )
        SwitchWithLabel(
            label = "Require Equal Hand Sizes",
            initialState = uiState.game.constraints.equalHandSizes,
            onCheckedChange = { newCheckedState ->
                onEvent(GameSetupUiEvent.SetConstraintEqualHandSizes(newCheckedState))
            },
            readOnly = uiState.mode == GameSetupUiState.Mode.VIEW,
            modifier = modifier
        )
        NullableIntOutlinedTextField(
            label = stringResource(R.string.multiple_of),
            number = uiState.game.constraints.multipleOf,
            onValueChange = { newValue ->
                onEvent(
                    GameSetupUiEvent.SetConstraintScoreModulus(
                        newValue
                    )
                )
            },
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

    var expanded by remember { mutableStateOf(true) }
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
    onEvent: (GameSetupUiEvent) -> Unit,
    modifier: Modifier
) {
    GameSectionCard(
        title = stringResource(R.string.objective),
        modifier = modifier
    ) {
        ObjectiveTypeDropMenu(
            value = uiState.game.objective.type,
            onObjectiveTypeChange = { type: Game.Objective.Type ->
                onEvent(
                    GameSetupUiEvent.SetObjectiveType(
                        type
                    )
                )
            },
            readOnly = uiState.mode == GameSetupUiState.Mode.VIEW,
            modifier = modifier
        )
        NullableIntOutlinedTextField(
            label = "Goal",
            number = uiState.game.objective.goal,
            onValueChange = { goal: Int? -> onEvent(GameSetupUiEvent.SetObjectiveGoal(goal)) },
            readOnly = uiState.mode == GameSetupUiState.Mode.VIEW,
            modifier = modifier
        )
        NullableIntOutlinedTextField(
            label = "Rounds",
            number = uiState.game.objective.rounds,
            onValueChange = { rounds: Int? -> onEvent(GameSetupUiEvent.SetObjectiveRounds(rounds)) },
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
    initialState: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    readOnly: Boolean,
    modifier: Modifier
) {
    GameSectionRow(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        val (checkedState, setCheckedState) = remember { mutableStateOf(initialState) }

        Switch(
            checked = checkedState,
            onCheckedChange = {
                setCheckedState(it)
                onCheckedChange(it)
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
        val (expanded, setExpanded) = remember { mutableStateOf(false) }
        val selectedText = toDisplayName(value)

        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = {
                if (!readOnly) {
                    setExpanded(!expanded)
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
                onDismissRequest = { setExpanded(false) }
            ) {
                Game.Objective.Type.entries.forEach { selectionOption ->
                    DropdownMenuItem(
                        text = { Text(toDisplayName(selectionOption)) },
                        onClick = {
                            onObjectiveTypeChange(selectionOption)
                            setExpanded(false)
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
        val (expanded, setExpanded) = remember { mutableStateOf(false) }
        val selectedText = toDisplayName(value)

        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = {
                if (!readOnly) {
                    setExpanded(!expanded)
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
                onDismissRequest = { setExpanded(false) }
            ) {
                Game.Colors.Color.entries.forEach { selectionOption ->
                    DropdownMenuItem(
                        text = { Text(toDisplayName(selectionOption)) },
                        onClick = {
                            onChange(selectionOption)
                            setExpanded(false)
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun NavigationIcon(onBack: () -> Unit) {
    IconButton(onClick = { onBack() }) { // Or navigateUp()
        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
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
        onEvent = { _ -> },
        modifier = Modifier
    )
}
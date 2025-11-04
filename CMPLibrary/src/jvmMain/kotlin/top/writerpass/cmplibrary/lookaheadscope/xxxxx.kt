@file:OptIn(ExperimentalAnimatableApi::class)

package top.writerpass.cmplibrary.lookaheadscope

import androidx.compose.animation.core.AnimationVector2D
import androidx.compose.animation.core.DeferredTargetAnimation
import androidx.compose.animation.core.ExperimentalAnimatableApi
import androidx.compose.animation.core.VectorConverter
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.movableContentOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.layout.ApproachLayoutModifierNode
import androidx.compose.ui.layout.ApproachMeasureScope
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.layout.LookaheadScope
import androidx.compose.ui.layout.Measurable
import androidx.compose.ui.layout.MeasureResult
import androidx.compose.ui.layout.Placeable
import androidx.compose.ui.node.ModifierNodeElement
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.round
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.singleWindowApplication
import top.writerpass.cmplibrary.compose.ables.Text

private object XXXXX {
    /**
     * Creates a custom implementation of ApproachLayoutModifierNode to approach the placement of
     * the layout using an animation.
     */
    class AnimatedPlacementModifierNode(var lookaheadScope: LookaheadScope) :
        ApproachLayoutModifierNode, Modifier.Node() {
        // Creates an offset animation, the target of which will be known during placement.
        val offsetAnimation: DeferredTargetAnimation<IntOffset, AnimationVector2D> =
            DeferredTargetAnimation(IntOffset.VectorConverter)

        override fun isMeasurementApproachInProgress(lookaheadSize: IntSize): Boolean {
            // Since we only animate the placement here, we can consider measurement approach
            // complete.
            return false
        }

        // Returns true when the offset animation is in progress, false otherwise.
        override fun Placeable.PlacementScope.isPlacementApproachInProgress(lookaheadCoordinates: LayoutCoordinates): Boolean {
            val target = with(lookaheadScope) {
                lookaheadScopeCoordinates.localLookaheadPositionOf(lookaheadCoordinates).round()
            }
            offsetAnimation.updateTarget(target, coroutineScope)
            return !offsetAnimation.isIdle
        }

        override fun ApproachMeasureScope.approachMeasure(
            measurable: Measurable,
            constraints: Constraints,
        ): MeasureResult {
            val placeable = measurable.measure(constraints)
            return layout(placeable.width, placeable.height) {
                val coordinates = coordinates
                if (coordinates != null) {
                    // Calculates the target offset within the lookaheadScope
                    val target = with(lookaheadScope) {
                        lookaheadScopeCoordinates.localLookaheadPositionOf(coordinates).round()
                    }

                    // Uses the target offset to start an offset animation
                    val animatedOffset = offsetAnimation.updateTarget(target, coroutineScope)
                    // Calculates the *current* offset within the given LookaheadScope
                    val placementOffset =
                        with(lookaheadScope) {
                            lookaheadScopeCoordinates
                                .localPositionOf(coordinates, Offset.Zero)
                                .round()
                        }
                    // Calculates the delta between animated position in scope and current
                    // position in scope, and places the child at the delta offset. This puts
                    // the child layout at the animated position.
                    val (x, y) = animatedOffset - placementOffset
                    placeable.place(x, y)
                } else {
                    placeable.place(0, 0)
                }
            }
        }
    }

    // Creates a custom node element for the AnimatedPlacementModifierNode above.
    data class AnimatePlacementNodeElement(val lookaheadScope: LookaheadScope) :
        ModifierNodeElement<AnimatedPlacementModifierNode>() {

        override fun update(node: AnimatedPlacementModifierNode) {
            node.lookaheadScope = lookaheadScope
        }

        override fun create(): AnimatedPlacementModifierNode {
            return AnimatedPlacementModifierNode(lookaheadScope)
        }
    }


    @Composable
    fun aaa() {
        var isInColumn by remember { mutableStateOf(true) }
        LookaheadScope {
            val hours = remember {
                movableContentOf {
                    listOf("1", "2").forEach {
                        it.Text(
                            modifier = Modifier.padding(8.dp)
                                .wrapContentSize()
                                .then(AnimatePlacementNodeElement(this@LookaheadScope)),
                            textAlign = TextAlign.Center,
                            fontSize = 150.sp
                        )
                    }
                }
            }

            val minutes = remember {
                movableContentOf {
                    listOf("5", "6").forEach {
                        it.Text(
                            modifier = Modifier.padding(8.dp)
                                .wrapContentSize()
                                .then(AnimatePlacementNodeElement(this@LookaheadScope)),
                            textAlign = TextAlign.Center,
                            fontSize = 150.sp
                        )
                    }
                }
            }

            Box(modifier = Modifier.fillMaxSize().clickable { isInColumn = !isInColumn }) {
                // As the items get moved between Column and Row, their positions in LookaheadScope
                // will change. The `animatePlacementInScope` modifier created above will
                // observe that final position change via `localLookaheadPositionOf`, and create
                // a position animation.
                if (isInColumn) {
                    Column(Modifier.align(Alignment.Center).wrapContentSize()) {
                        Row { hours() }
                        Row { minutes() }
                    }
                } else {
                    Row(modifier = Modifier.align(Alignment.TopStart).wrapContentSize()) {
                        hours()
                        Spacer(modifier = Modifier.width(20.dp))
                        minutes()
                    }
                }
            }
        }
    }
}

fun main() = singleWindowApplication {
    Column {
        Box(
            modifier = Modifier.fillMaxWidth()
                .weight(1f)
        ) {
            XXXXX.aaa()
        }
    }
}


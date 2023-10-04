package fr.uha.hassenforder.android.icons;

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.materialIcon
import androidx.compose.material.icons.materialPath
import androidx.compose.ui.graphics.vector.ImageVector

public val Icons.Outlined.Agender: ImageVector
    get() {
        if (_agender != null) {
            return _agender!!
        }
        _agender = materialIcon(name = "Outlined.Agender") {
            materialPath {
                moveTo(9.5f, 11.0f)
                curveToRelative(1.93f, 0.0f, 3.5f, 1.57f, 3.5f, 3.5f)
                reflectiveCurveTo(11.43f, 18.0f, 9.5f, 18.0f)
                reflectiveCurveTo(6.0f, 16.43f, 6.0f, 14.5f)
                reflectiveCurveTo(7.57f, 11.0f, 9.5f, 11.0f)
                close()
                moveTo(9.5f, 9.0f)
                curveTo(6.46f, 9.0f, 4.0f, 11.46f, 4.0f, 14.5f)
                reflectiveCurveTo(6.46f, 20.0f, 9.5f, 20.0f)
                reflectiveCurveToRelative(5.5f, -2.46f, 5.5f, -5.5f)
                curveToRelative(0.0f, -1.16f, -0.36f, -2.23f, -0.97f, -3.12f)
                lineTo(18.0f, 7.42f)
                verticalLineTo(10.0f)
                horizontalLineToRelative(2.0f)
                verticalLineTo(4.0f)
                horizontalLineToRelative(-6.0f)
                verticalLineToRelative(2.0f)
                horizontalLineToRelative(2.58f)
                lineToRelative(-3.97f, 3.97f)
                curveTo(11.73f, 9.36f, 10.66f, 9.0f, 9.5f, 9.0f)
                close()
            }
        }
        return _agender!!
    }

private var _agender: ImageVector? = null

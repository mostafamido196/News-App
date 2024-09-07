package com.samy.zonakchallenge.presentation.detail_screen

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.view.WindowInsets
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavController
import com.samy.zonakchallenge.R
import com.samy.zonakchallenge.domain.model.NewsResponse
import com.samy.zonakchallenge.ui.theme.Orange
import com.samy.zonakchallenge.utils.sdp
import com.samy.zonakchallenge.utils.ssp

import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat

@Composable
fun DetailsScreen(
    navController: NavController,
    data: NewsResponse.Source = NewsResponse.Source(
        id = "aftenposten",
        name = "Aftenposten",
        description = "Norges ledende nettavis med alltid oppdaterte nyheter innenfor innenriks, utenriks, sport og kultur.",
        url = "https://www.aftenposten.no",
        category = "general",
        language = "no",
        country = "no"
    ),
) {

    HiddenStatuesBar()

    Column(
        Modifier
            .background(Color.White)
            .fillMaxSize()
    ) {
        ImgCategory(navController, data)
        TextTitle(data)
        TextDescription(data)
    }
}

@SuppressLint("WrongConstant")
@Composable
fun HiddenStatuesBar() {
    val context = LocalContext.current
    val view = LocalView.current
    val activity = context as? Activity
    if (activity != null) {
        val windowInsetsController =
            WindowInsetsControllerCompat(activity.window, activity.window.decorView)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {

            SideEffect {
                // Hide the status bar
                windowInsetsController.hide(WindowInsets.Type.statusBars())
            }

            DisposableEffect(Unit) {
                onDispose {
                    // Show the status bar again when leaving the screen
                    windowInsetsController.show(WindowInsets.Type.statusBars())
                }
            }
        }
    }

}

@Composable
fun TextDescription(data: NewsResponse.Source) {
    val context = LocalContext.current
    Text(
        text = data.description,
        style = TextStyle(
            fontSize = 6.ssp(),
            color = Color.Black
        ),
        modifier = Modifier
            .clickable {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(data.url))
                context.startActivity(intent) // Launch the browser
            }
            .wrapContentHeight()
            .padding(start = 10.sdp())
    )
}

@Composable
fun TextTitle(data: NewsResponse.Source) {
    val context = LocalContext.current
    Row(
        modifier = Modifier
            .wrapContentHeight()
            .fillMaxWidth()
            .padding(start = 8.sdp(), end = 8.sdp(), top = 6.sdp(), bottom = 6.sdp())
            .height(IntrinsicSize.Min) // Make the row as tall as its content
            .clickable {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(data.url))
                context.startActivity(intent) // Launch the browser
            }) {
        // Left side - Point (Circle) and Line below it
        Canvas(
            modifier = Modifier
                .fillMaxHeight()
                .width(6.sdp())
        ) {
            // Draw a line that matches the height of the text
            drawLine(
                color = Orange,
                start = Offset(size.width / 2, 0f),
                end = Offset(size.width / 2, size.height),
                strokeWidth = 12f
            )
        }

        // Content on the right (Text)
        Text(
            text = data.name,
            style = TextStyle(
                fontSize = 12.ssp(),
                fontWeight = FontWeight.Bold,
                color = Color.Black
            ),
            modifier = Modifier
                .clickable {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(data.url))
                    context.startActivity(intent) // Launch the browser
                }
                .wrapContentHeight()
                .padding(start = 2.sdp())
        )
    }
}


@Composable
fun ImgCategory(navController: NavController, data: NewsResponse.Source) {
    val context = LocalContext.current
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.sdp())
            .clip(RoundedCornerShape(4.sdp())),
        contentAlignment = Alignment.Center
    ) {
        // Image
        Image(
            painter = getPointer(data.category),
            contentDescription = "img category",
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(4.sdp()))
                .background(Color.Transparent),
            contentScale = ContentScale.Crop // Adjust based on your needs
        )

        // Description at the bottom
        Box(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(start = 8.sdp(), bottom = 8.sdp())
        ) {
            Text(
                text = "2 hours ago",
                color = Color.White,//WhiteLow
                style = TextStyle(fontSize = 6.ssp())
            )
        }

        // Text on the top-left corner
        Box(
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(start = 6.sdp(), top = 8.sdp())
                .clickable {
                    navController.popBackStack()
                }
        ) {
            Image(
                painter = painterResource(R.drawable.baseline_arrow_back_ios_new_24),
                contentDescription = "img category",
            )
        }
        // Save icon on the top-right corner
        Row(
            modifier = Modifier
                .clickable {

                }
                .align(Alignment.TopEnd)
                .padding(top = 8.sdp())
        ) {
            Image(
                painter = painterResource(id = R.drawable.share),
                contentDescription = "share Icon",
                modifier = Modifier
                    .clickable {
                        // Create the sharing intent
                        val shareIntent = Intent(Intent.ACTION_SEND).apply {
                            type = "text/plain"
                            putExtra(Intent.EXTRA_TEXT, data.url) // The link to be shared
                        }

                        // Launch the chooser to select an app to share the link with
                        context.startActivity(
                            Intent.createChooser(shareIntent, "Share link via")
                        )
                    }
                    .width(8.sdp())
                    .height(8.sdp())
                    .size(16.sdp())
            )
            Spacer(modifier = Modifier.width(6.sdp()))
            Image(
                painter = painterResource(id = R.drawable.bookmark_white),
                contentDescription = "Saved Icon",
                modifier = Modifier
                    .width(8.sdp())
                    .height(8.sdp())
                    .padding(1.sdp())
                    .size(16.sdp()) // Size for the saved icon
            )
            Spacer(modifier = Modifier.width(6.sdp()))
        }

        //icons bottom right
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(top = 12.sdp(), bottom = 8.sdp())
        ) {
            Image(
                painter = painterResource(id = R.drawable.message),
                contentDescription = "share Icon",
                modifier = Modifier
                    .width(8.sdp())
                    .height(8.sdp())
                    .padding(top = 1.sdp())
                    .size(16.sdp())
            )
            Spacer(modifier = Modifier.width(2.sdp()))
            Text(
                modifier = Modifier
                    .padding(top = 1.sdp()),
                text = "27",
                style = TextStyle(color = Color.White)
            )
            Spacer(modifier = Modifier.width(6.sdp()))
            Image(
                painter = painterResource(id = R.drawable.baseline_visibility_24),
                contentDescription = "Saved Icon",
                modifier = Modifier
                    .padding(top = 1.sdp())
                    .width(8.sdp())
                    .height(8.sdp())
                    .size(16.sdp()) // Size for the saved icon
            )
            Spacer(modifier = Modifier.width(2.sdp()))
            Text(
                modifier = Modifier
                    .padding(top = 1.sdp()), text = "916",
                style = TextStyle(color = Color.White)
            )
            Spacer(modifier = Modifier.width(6.sdp()))
        }
    }
}

@Composable
fun getPointer(category: String): Painter {
    return when (category) {
        "general" -> painterResource(id = R.drawable.gneral2)
        "technology" -> painterResource(id = R.drawable.tech2)
        "sports" -> painterResource(id = R.drawable.sport2)
        "business" -> painterResource(id = R.drawable.business)
        "entertainment" -> painterResource(id = R.drawable.intertainment2)
        "health" -> painterResource(id = R.drawable.health)
        "science" -> painterResource(id = R.drawable.scince1)
        else -> painterResource(id = R.drawable.general)
    }
}

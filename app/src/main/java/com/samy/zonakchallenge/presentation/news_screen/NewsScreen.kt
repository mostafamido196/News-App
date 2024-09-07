package com.samy.zonakchallenge.presentation.news_screen

import android.net.Uri
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import com.samy.zonakchallenge.R
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.rememberPagerState
import com.google.gson.Gson
import com.samy.zonakchallenge.domain.model.NewsResponse
import com.samy.zonakchallenge.ui.theme.Black
import com.samy.zonakchallenge.ui.theme.FontColorGray
import com.samy.zonakchallenge.ui.theme.Orange
import com.samy.zonakchallenge.ui.theme.WhiteLow

import com.samy.zonakchallenge.utils.sdp
import com.samy.zonakchallenge.utils.ssp
import com.samy.zonakchallenge.utils.truncatedText
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.net.URLEncoder


@Composable
fun NewsScreen(
    navController: NavController,
    viewModel: NewsViewModel = hiltViewModel(),
) {

    viewModel.sendIntent(MainIntents.GetNews)
    val state by viewModel.newsState.collectAsState()
    println("mos state: $state")
    when (state) {
        is DataState.Loading -> {
            // Show a loading spinner
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxSize()
            ) {
                CircularProgressIndicator()
            }
        }

        is DataState.Result<*> -> {
            NewsScreenContent(
                (state as DataState.Result<*>).data as List<NewsResponse.Source>,
                navController
            )
        }

        is DataState.Error -> {
            // Show error message
            Text(
                text = (state as DataState.Error).msg,
                modifier = Modifier
                    .fillMaxSize()
                    .wrapContentSize(Alignment.Center)
            )
        }

        else -> {
            // Idle state, maybe prompt the user to add a number

        }
    }
}

@Composable
fun NewsScreenContent(
    news: List<NewsResponse.Source>,
    navController: NavController,
    viewModel: NewsViewModel = hiltViewModel(),
) {
    var selectedCategory by remember { mutableStateOf(news[0].category) }
    Surface(
        color = Color.White,
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.White)

    ) {
        Column {
            Spacer(modifier = Modifier.height(14.sdp()))
            LogoImg()
            Spacer(modifier = Modifier.height(6.sdp()))
            CategoryList(
                viewModel.getDifferenceCategories(news),
                onCategorySelected = { selectedItem ->
                    selectedCategory = selectedItem
                })
            BeautifulList()
            Spacer(modifier = Modifier.height(8.sdp()))
            CategoryTitle(title = selectedCategory)
            NewsList(viewModel.getCategorySelectedList(news, selectedCategory), navController)
        }
    }
}

@Composable
fun LogoImg() {
    Box(
        modifier = Modifier
            .padding(start = 8.sdp())
            .clip(RoundedCornerShape(2.sdp()))
    ) {
        Image(
            painter = painterResource(id = R.drawable.fff),
            contentDescription = "logo of screen",
            contentScale = ContentScale.Crop, // Optionally crop the image
            modifier = Modifier
                .height(12.sdp())
                .wrapContentWidth(),
        )
    }
}

@Composable
fun CategoryList(
    categories: List<String> = listOf(
        "general",
        "technology",
        "sport",
        "general",
        "technology",
        "sport",
    ),
    onCategorySelected: ((String) -> Unit)? = null,
) {
    var selectedIndex by remember {
        mutableStateOf(0)
    } // Initially select the first item

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = Color.White)
    ) {
        LazyRow{
            items(categories.size) { index ->
                ListItem(
                    title = categories[index],
                    isSelected = selectedIndex == index,
                    onClick = {
                        selectedIndex = index
                        if (onCategorySelected != null) {
                            onCategorySelected(categories[index])
                        } // Notify the selected category
                    }
                )
            }
        }
    }
}

@Composable
fun ListItem(title: String, isSelected: Boolean, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .background(color = Color.White)
    ) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(start = 4.sdp(), end = 4.sdp(), top = 2.sdp(), bottom = 2.sdp())
            .background(color = Color.White)
            .clickable(onClick = onClick)
            .width(IntrinsicSize.Min) // Make the row as tall as its content
    ) {
        Text(
            text = title,
            style = TextStyle(
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                fontSize = 6.ssp(), color = FontColorGray
            ),
            modifier = Modifier
                .background(color = Color.White)
                .wrapContentWidth()
        )
        Spacer(modifier = Modifier.height(1.sdp()))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.sdp())
                    .background(if (isSelected)Orange else Color.White)
            )
    }}
}

@Composable
fun BeautifulList() {
    // Number of pages
    val pages = listOf(
        "Protecting our natural spaces",
        "we stop over utilising the resources given to us by nature",
        "How Much of the World Will Be Quarantined by the Coronavirus?"
    )

    // Pager state to control the current page
    val pagerState = rememberPagerState()

    LaunchedEffect(pagerState) {
        launch {
            while (pagerState.currentPage < pages.size - 1) {
                delay(1000)
                val nextPage = pagerState.currentPage + 1
                pagerState.animateScrollToPage(
                    page = nextPage,
                    animationSpec = tween(
                        durationMillis = 5000
                    )
                )
            }
        }
    }
    ImgButiful(pages, pagerState)
    Spacer(modifier = Modifier.height(4.sdp()))

    // Page Indicators
    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        repeat(pages.size) { page ->
            // Set color for the current page and style for others
            val color = if (pagerState.currentPage == page) Color.Transparent else Orange
            val borderColor = if (pagerState.currentPage == page) Orange else Orange

            Box(
                modifier = Modifier
                    .padding(2.sdp())
                    .size(4.sdp())
                    .border(
                        1.sdp(),
                        borderColor,
                        shape = CircleShape
                    ) // Apply border for non-current pages
                    .background(color, shape = CircleShape) // Background only for current page
            )
        }
    }
}

@Composable
fun ImgButiful(pages: List<String>, pagerState: PagerState) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Horizontal Pager
        HorizontalPager(
            count = pages.size,
            state = pagerState,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 2.sdp(), start = 4.sdp(), end = 4.sdp())
                .height(72.sdp()) // Adjust height as needed
        ) { page ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(4.sdp())),
                contentAlignment = Alignment.Center
            ) {
                // Image
                Image(
                    painter = getImgUrl(page),
                    contentDescription = "Source Image",
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
                        .padding(start = 4.sdp(), end = 4.sdp(), bottom = 6.sdp())
                ) {
                    Text(
                        text = pages[currentPage],
                        color = WhiteLow,
                        style = TextStyle(fontSize = 8.ssp())
                    )
                }

                // Text on the top-left corner
                Box(
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(start = 4.sdp(), top = 6.sdp())
                ) {
                    Text(
                        text = getTimeOldDemo(currentPage), // Change as needed
                        color = Color.White,
                        style = TextStyle(fontSize = 4.ssp())
                    )
                }
                var isSaved by remember { mutableStateOf(false) }

                // Save icon on the top-right corner
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(end = 4.sdp(), top = 6.sdp())
                ) {
                    Image(
                        painter = painterResource(id = if (isSaved) R.drawable.bookmark_white else R.drawable.bookmark_empty_white),
                        contentDescription = "Saved Icon",
                        modifier = Modifier
                            .clickable {
                                isSaved = !isSaved
                            }
                            .width(8.sdp())
                            .height(8.sdp())
                            .size(16.sdp()) // Size for the saved icon
                    )
                }
            }
        }
    }
}

fun getTimeOldDemo(currentPage: Int): String {
    return if (currentPage == 0) "2 Hours ago"
    else if (currentPage == 1) "90 Minutes ago"
    else "1 Hour ago"

}


@Composable
fun getImgUrl(page: Int): Painter {
    return when (page) {
        0 -> painterResource(id = R.drawable.person)
        1 -> painterResource(id = R.drawable.goood)
        else -> painterResource(id = R.drawable.die)
    }
}

@Composable
fun CategoryTitle(title: String) {
    Text(
        text = title,
        fontSize = 12.ssp(),
        style = TextStyle(
            color = Black,
            fontWeight = FontWeight.Bold
        ),
        modifier = Modifier.padding(start = 12.sdp())
    )
}

@Composable
fun NewsList(
    categorySelectedList: List<NewsResponse.Source>,
    navController: NavController,
) {
    // Remember the scroll state
    val listState = rememberLazyListState()
    // Trigger scroll to the top when the list changes
    LaunchedEffect(categorySelectedList) {
        listState.scrollToItem(0)
    }

    // State to track saved items
    var savedItems by remember { mutableStateOf(setOf<String>()) }

    LazyColumn(
        state = listState
    ) {
        items(categorySelectedList.size) { index ->
            val dataItem = categorySelectedList[index]
            ListItemSelectedCategory(
                data = dataItem,
                onClick = {
                    val jsonString = Uri.encode(Gson().toJson(dataItem))
                    navController.navigate("details_screen/$jsonString")
                },
                isSaved = savedItems.contains(dataItem.id),
                onSaveClick = { isSaved ->
                    savedItems = if (isSaved) {
                        savedItems + dataItem.id
                    } else {
                        savedItems - dataItem.id
                    }
                }
            )
        }
    }
}

@Composable
fun ListItemSelectedCategory(
    data: NewsResponse.Source,
    onClick: () -> Unit,
    isSaved: Boolean,
    onSaveClick: (Boolean) -> Unit,
) {
    Box(
        modifier = Modifier
            .clickable(onClick = onClick)
            .padding(
                horizontal = 8.sdp(),
                vertical = 1.sdp()
            )
            .fillMaxWidth()
            .height(32.sdp())
    ) {
        Row(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth()
        ) {
            // Left side - Point (Circle) and Line below it
            Column(
                modifier = Modifier
                    .wrapContentWidth()
                    .padding(top = 1.sdp())
                    .fillMaxHeight(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Circle (Point)
                Canvas(
                    modifier = Modifier
                        .size(6.sdp())
                ) {
                    drawCircle(
                        color = Orange,  // Customize the point color
                        radius = size.minDimension / 2
                    )
                }

                // Line below the circle (unless it's the last item)
                Canvas(
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(6.sdp())
                        .padding(bottom = 6.sdp())
                ) {
                    drawLine(
                        color = Orange,  // Customize the line color
                        start = Offset(size.width / 2, 0f),
                        end = Offset(size.width / 2, size.height),
                        strokeWidth = 6f  // Reduce stroke width for a thinner line
                    )
                }
            }


            // content on right
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(1f)
                    .padding(
                        bottom = 2.sdp(),
                        start = 2.sdp()
                    )
            ) {
                Text(
                    text = data.name,
                    style = TextStyle(
                        fontSize = 6.ssp(),
                        fontWeight = FontWeight.Bold,
                        color = Black
                    ),
                    modifier = Modifier
                        .padding(bottom = 2.sdp())
                )
                Spacer(modifier = Modifier.weight(1f))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 2.sdp())
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxHeight()
                            .weight(1f)// between bookmark img and sub title
                    ) {
                        Text(
                            text = data.description.truncatedText(120),
                            style = TextStyle(
                                fontSize = 4.ssp(),
                                color = Color.Gray
                            ),
                            modifier = Modifier
                                .align(Alignment.TopStart)
                                .padding(
                                    start = 2.sdp(),
                                    end = 6.sdp()
                                )
                        )
                    }

                    Box(
                        modifier = Modifier
                            .fillMaxHeight()
                            .wrapContentWidth()
                    ) {
                        Image(
                            painter = painterResource(id = if (isSaved) R.drawable.bookmark else R.drawable.bookmark_empty),
                            contentDescription = "Saved Icon",
                            modifier = Modifier
                                .clickable {
                                    onSaveClick(!isSaved)
                                }
                                .align(Alignment.TopEnd)
                                .width(8.sdp())
                                .height(8.sdp())
                                .size(16.sdp())
                        )
                    }
                }
            }
        }
    }


}






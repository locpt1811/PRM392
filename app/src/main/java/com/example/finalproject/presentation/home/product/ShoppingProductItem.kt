package com.example.finalproject.presentation.home.product

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.finalproject.R
import com.example.finalproject.model.shopping.BookDTO
import com.example.finalproject.model.shopping.BookLanguage
import com.example.finalproject.model.shopping.Category
import com.example.finalproject.model.shopping.Publisher

private val imageModifier = Modifier
    .fillMaxWidth()
    .height(128.dp)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShoppingProductItem(
    book_id: Int,
    title: String?,
    isbn13: String?,
    num_pages: Int?,
    image_url: String?,
    description: String?,
    rating: Double?,
    price: Double?,
    user_id: String?,

    language_id: Int?,
    language_code: String?,
    language_name: String?,

    publisher_id: Int?,
    publisher_name: String?,

    category_id: Int?,
    category_name: String?,

    onProductClick: (BookDTO) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxSize(),
        onClick = remember { { onProductClick(
            BookDTO(
                book_id,
                title,
                isbn13,
                num_pages,
                image_url,
                description,
                rating,
                price,
                user_id,
                BookLanguage(language_id, language_code, language_name),
                Category(category_id, category_name),
                Publisher(publisher_id, publisher_name),
                )
        ) } },
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(
            1.dp, Brush.horizontalGradient(
                listOf(
                    colorResource(id = R.color.dark_green),
                    colorResource(id = R.color.hunter_green),
                    colorResource(id = R.color.dark_moss_green),
                    colorResource(id = R.color.walnut_brown),
                    colorResource(id = R.color.bole),
                    colorResource(id = R.color.cordovan),
                    colorResource(id = R.color.redwood)
                )
            )
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(dimensionResource(id = R.dimen.one_level_margin)),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AsyncImage(
                modifier = imageModifier,
                model = ImageRequest.Builder(LocalContext.current)
                    .data(image_url)
                    .crossfade(true)
                    .build(),
                error = painterResource(id = R.drawable.error_img),
                contentDescription = null,
                contentScale = ContentScale.Fit,
                placeholder = if (LocalInspectionMode.current) painterResource(id = R.drawable.debug_placeholder) else null
            )
            MinLineText(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = dimensionResource(id = R.dimen.one_level_margin))
                    .padding(horizontal = dimensionResource(id = R.dimen.one_level_margin)),
                text = title ?: "null",
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                minLines = 2,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                color = Color.Black
            )
            Text(
                modifier = Modifier.padding(top = dimensionResource(id = R.dimen.one_level_margin)),
                text = "$${price}",
                color = Color.Black
            )
        }
    }
}

@Composable
private fun MinLineText(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = Color.Unspecified,
    fontSize: TextUnit = TextUnit.Unspecified,
    fontStyle: FontStyle? = null,
    fontWeight: FontWeight? = null,
    fontFamily: FontFamily? = null,
    letterSpacing: TextUnit = TextUnit.Unspecified,
    textDecoration: TextDecoration? = null,
    textAlign: TextAlign? = null,
    lineHeight: TextUnit = TextUnit.Unspecified,
    overflow: TextOverflow = TextOverflow.Ellipsis,
    softWrap: Boolean = true,
    maxLines: Int = Int.MAX_VALUE,
    minLines: Int = 0,
    onTextLayout: (TextLayoutResult) -> Unit = {},
    style: TextStyle = LocalTextStyle.current
) {
    var mText by remember { mutableStateOf(text) }

    Text(
        mText,
        modifier,
        color,
        fontSize,
        fontStyle,
        fontWeight,
        fontFamily,
        letterSpacing,
        textDecoration,
        textAlign,
        lineHeight,
        overflow,
        softWrap,
        maxLines,
        minLines,
        {
            if (it.lineCount < minLines) {
                mText = text + "\n".repeat(minLines - it.lineCount)
            }
            onTextLayout(it)
        },
        style,
    )
}
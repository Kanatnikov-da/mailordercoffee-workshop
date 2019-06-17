package nl.testchamber.mailordercoffeeshop.orderfinished

import androidx.appcompat.app.AppCompatActivity

import android.os.Bundle
import kotlinx.android.synthetic.main.activity_order_finished.*

import nl.testchamber.mailordercoffeeshop.R
import nl.testchamber.mailordercoffeeshop.data.Order

class OrderFinishedActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_finished)
        val order = intent.getParcelableExtra<Order>(Order.PARCEL_NAME)
        val beverageIngredientsListing = order.ingredients.joinToString("\n", prefix = "Ingredients:\n") { it.ingredientName.trim() }

        name_field.setText("Name: ${order.customerName}")
        email_field.setText("E-mail: ${order.customerEmail}")
        beverage_name_field.setText(order.orderName)
        ingredients_field.setText(beverageIngredientsListing)

    }
}

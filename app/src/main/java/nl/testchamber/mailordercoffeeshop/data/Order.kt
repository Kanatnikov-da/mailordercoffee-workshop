package nl.testchamber.mailordercoffeeshop.data

import android.os.Parcel
import android.os.Parcelable
import nl.testchamber.mailordercoffeeshop.data.beverage.Ingredient

data class Order(val customerName: String, val customerEmail: String, val orderName: String, val ingredients: List<Ingredient>): Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.createTypedArrayList(Ingredient)) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(customerName)
        parcel.writeString(customerEmail)
        parcel.writeString(orderName)
        parcel.writeTypedList(ingredients)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Order> {
        override fun createFromParcel(parcel: Parcel): Order {
            return Order(parcel)
        }

        override fun newArray(size: Int): Array<Order?> {
            return arrayOfNulls(size)
        }

        const val PARCEL_NAME = "order"
    }

}
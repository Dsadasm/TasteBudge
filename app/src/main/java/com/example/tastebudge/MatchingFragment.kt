package com.example.tastebudge

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.yuyakaido.android.cardstackview.CardStackLayoutManager
import com.yuyakaido.android.cardstackview.CardStackView
import com.yuyakaido.android.cardstackview.Direction
import com.yuyakaido.android.cardstackview.StackFrom
import com.yuyakaido.android.cardstackview.SwipeableMethod


class MatchingFragment : Fragment() {
    private var tasteBudgeGame : TasteBudgeGame? = null
    private lateinit var cardStackView: CardStackView
    private lateinit var adapter: RestaurantCardAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_matching, container, false)

        // Fetch restaurant from current session
        TasteBudgeManager.fetchGame()
        TasteBudgeManager.tasteBudgeGame.observe(viewLifecycleOwner) {
            tasteBudgeGame = it
            tasteBudgeGame?.apply {
                adapter = RestaurantCardAdapter(this.restaurantList)
                cardStackView = view.findViewById(R.id.card_stack_view)
                cardStackView.layoutManager = CardStackLayoutManager(requireContext())
                cardStackView.adapter = adapter
                setupCardStackView()
            }
        }
        // Inflate the layout for this fragment
        return view
    }

    private fun setupCardStackView() {
        // Configure CardStackView
        val layoutManager = cardStackView.layoutManager as CardStackLayoutManager
        layoutManager.setStackFrom(StackFrom.None)
        layoutManager.setVisibleCount(3)
        layoutManager.setTranslationInterval(8.0f)
        layoutManager.setScaleInterval(0.95f)
        layoutManager.setSwipeThreshold(0.3f)
        layoutManager.setMaxDegree(20.0f)
        layoutManager.setDirections(Direction.HORIZONTAL)
        layoutManager.setCanScrollHorizontal(true)
        layoutManager.setCanScrollVertical(true)
        layoutManager.setSwipeableMethod(SwipeableMethod.AutomaticAndManual)

        // Set swipe listener
//        cardStackView.setCardStackListener(object : CardStackListener {
//            override fun onCardDragging(direction: Direction, ratio: Float) {
//                // Show swipe indicators while dragging
//                updateSwipeIndicators(direction, ratio)
//            }
//
//            override fun onCardSwiped(direction: Direction) {
//                val swipedPosition = cardStackView.topIndex - 1
//                if (swipedPosition >= 0 && swipedPosition < restaurantList.size) {
//                    val restaurant = restaurantList[swipedPosition]
//                    handleCardSwiped(restaurant, direction)
//                }
//
//                // Hide indicators after swipe
//                hideSwipeIndicators()
//
//                // Check if we need more restaurants
//                checkLoadMoreRestaurants()
//            }
//
//            override fun onCardRewound() {
//                // Handle rewind
//                hideSwipeIndicators()
//            }
//
//            override fun onCardCanceled() {
//                // Card returned to center
//                hideSwipeIndicators()
//            }
//
//            override fun onCardAppeared(view: View, position: Int) {
//                // Card appeared at top
//            }
//
//            override fun onCardDisappeared(view: View, position: Int) {
//                // Card disappeared from view
//            }
//        })
    }
}
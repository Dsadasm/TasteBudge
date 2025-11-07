package com.example.tastebudge

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.LinearInterpolator
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.yuyakaido.android.cardstackview.CardStackLayoutManager
import com.yuyakaido.android.cardstackview.CardStackListener
import com.yuyakaido.android.cardstackview.CardStackView
import com.yuyakaido.android.cardstackview.Direction
import com.yuyakaido.android.cardstackview.Duration
import com.yuyakaido.android.cardstackview.StackFrom
import com.yuyakaido.android.cardstackview.SwipeAnimationSetting
import com.yuyakaido.android.cardstackview.SwipeableMethod
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MatchingFragment : Fragment() {
    private var tasteBudgeGame : TasteBudgeGame? = null
    private lateinit var cardStackView: CardStackView
    private lateinit var cardStackLayoutManager: CardStackLayoutManager
    private lateinit var adapter: RestaurantCardAdapter
    private lateinit var feedbackView: TextView
    private lateinit var btnNope: ImageButton
    private lateinit var btnLike: ImageButton
    private lateinit var btnSuperLike: ImageButton

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_matching, container, false)
        feedbackView = view.findViewById(R.id.swipe_feedback)
        btnNope = view.findViewById(R.id.btn_dislike)
        btnLike = view.findViewById(R.id.btn_like)
        btnSuperLike = view.findViewById(R.id.btn_up)
        setupActionButtons()

        // Fetch restaurant from current session
        TasteBudgeManager.fetchGame()
        TasteBudgeManager.tasteBudgeGame.observe(viewLifecycleOwner) {
            tasteBudgeGame = it
            tasteBudgeGame?.apply {
                adapter = RestaurantCardAdapter(this.restaurantList)
                cardStackView = view.findViewById(R.id.card_stack_view)
                setupCardStackView(this.restaurantList)
            }
        }

        // Inflate the layout for this fragment
        return view
    }

    private fun setupCardStackView(restaurantList: List<Restaurant>) {
        // Create and configure the layout manager
        cardStackLayoutManager = CardStackLayoutManager(requireContext(), object : CardStackListener {
            override fun onCardDragging(
                direction: Direction?,
                ratio: Float
            ) {
                return
            }

            override fun onCardSwiped(direction: Direction?) {
                val swipedPosition = cardStackLayoutManager.topPosition - 1
                if (swipedPosition >= 0 && swipedPosition < restaurantList.size) {
                    direction?.let { handleCardSwiped(swipedPosition, it) }
                }
            }

            override fun onCardRewound() {
                return
            }

            override fun onCardCanceled() {
                return
            }

            override fun onCardAppeared(view: View?, position: Int) {
                return
            }

            override fun onCardDisappeared(view: View?, position: Int) {
                // Go to end screen if done voting
                if (position == restaurantList.size - 1) {
                    tasteBudgeGame?.apply {
                        TasteBudgeManager.saveGame(this)
                    }
                    val fragment = EndFragment()
                    val ft = parentFragmentManager.beginTransaction()
                    ft.replace(R.id.fragment_container_view, fragment)
                    ft.addToBackStack(null)
                    ft.commit()
                }
            }

        })

        // Configure layout manager settings
        val directions = listOf(Direction.Top, Direction.Left, Direction.Right)
        with(cardStackLayoutManager) {
            setStackFrom(StackFrom.None)
            setVisibleCount(3)
            setTranslationInterval(8.0f)
            setScaleInterval(0.95f)
            setSwipeThreshold(0.3f)
            setMaxDegree(20.0f)
            setDirections(directions)
            setCanScrollHorizontal(true)
            setCanScrollVertical(true)
            setSwipeableMethod(SwipeableMethod.AutomaticAndManual)
            setOverlayInterpolator(LinearInterpolator())
        }

        // Set layout manager to CardStackView
        cardStackView.layoutManager = cardStackLayoutManager

        // Set adapter
        adapter = RestaurantCardAdapter(restaurantList)
        cardStackView.adapter = adapter
    }

    private fun setupActionButtons() {
        btnNope.setOnClickListener {
            // Swipe left
            val direction = Direction.Left
            cardStackLayoutManager.setSwipeAnimationSetting(
                SwipeAnimationSetting.Builder()
                    .setDirection(direction)
                    .setDuration(Duration.Normal.duration)
                    .build()
            )
            cardStackView.swipe()
        }

        btnLike.setOnClickListener {
            // Swipe right
            val direction = Direction.Right
            cardStackLayoutManager.setSwipeAnimationSetting(
                SwipeAnimationSetting.Builder()
                    .setDirection(direction)
                    .setDuration(Duration.Normal.duration)
                    .build()
            )
            cardStackView.swipe()
        }

        btnSuperLike.setOnClickListener {
            // Swipe up for super like
            val direction = Direction.Top
            cardStackLayoutManager.setSwipeAnimationSetting(
                SwipeAnimationSetting.Builder()
                    .setDirection(direction)
                    .setDuration(Duration.Normal.duration)
                    .build()
            )
            cardStackView.swipe()
        }
    }

    private fun handleCardSwiped(swipePosition: Int, direction: Direction) {
        val vote: Int = when (direction) {
            Direction.Right -> 1
            Direction.Left -> 0
            Direction.Top -> 2
            else -> 0
        }

        // Send vote to Firebase
        tasteBudgeGame?.apply {
            restaurantList[swipePosition].score += vote
        }


        // Show swipe feedback
        showSwipeFeedback(direction)
    }

    private fun showSwipeFeedback(direction: Direction) {
        when (direction) {
            Direction.Right -> {
                feedbackView.text = "👍 Liked!"
                feedbackView.setBackgroundColor(Color.GREEN)
            }
            Direction.Left -> {
                feedbackView.text = "👎 Disliked"
                feedbackView.setBackgroundColor(Color.RED)
            }
            Direction.Top -> {
                feedbackView.text = "⭐ Super Liked!"
                feedbackView.setBackgroundColor(Color.BLUE)
            }
            else -> return
        }

        feedbackView.visibility = View.VISIBLE
        feedbackView.alpha = 0f
        feedbackView.animate()
            .alpha(1f)
            .setDuration(300)
            .withEndAction {
                feedbackView.animate()
                    .alpha(0f)
                    .setDuration(300)
                    .setStartDelay(500)
                    .start()
            }
            .start()
    }
}
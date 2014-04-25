float-view
==========
Float View(s) is becoming more and more popular in Chinese market, I hope this will help anyone having the same requirement.

A floating view is added onto the screen. It can be dragged to anywhere, but when your finger leaves the screen, it will move to the left or right side automatically, based on the place of leaving point. When clicked, a layout will spread to the right/left and some buttons are available. The spread layout will fold automatically if no action is implemented for a period of time. And it will change to be grey if automatically in 2 seconds.

Design
The floating view is added to WindowManager in FloatService, which keeps running at background. It is shown independently, that means it has no relationship with any Activity.

A "State Pattern" is implemented in this demo. It is used to control three states of the floating view:
StateInactive: the floating view is not active, it will be greyed and stay aside of the screen. The state will be changed to StateActiveFolded if the view is touched.
StateActiveFolded: the floating view is active, can be dragged to anywhere or spread its hidden buttons when clicked.
StateActiveSpread: the floating view is active, and the buttons are spread out and can be clicked.
In every State, there are three methods: onInit(), onDrag(), and onClick(), the actions in different states make the management of states easier. About Status Pattern:http://en.wikipedia.org/wiki/State_pattern.

SpreadLinearLayout is a sub class of LinearLayout.
The only change is adding of two methods: setPaddingRight(int paddingRight) and setPaddingLeft(int paddingLeft), which are used to help implementing a property animation.
ObjectAnimator is used in FloatViewGroup to implement the effect of spreading out and folding. A trick here is using negative paddingLeft and paddingRight in the spreading layout.
About Property Animation:http://developer.android.com/guide/topics/graphics/prop-animation.html.

TODO:
1  All the animations are running in a linear way, it will be better if some vivid animations added.
2  Spread layout can only spread to right or left, find a way to make it spread to any angle.
3  Buttons in the spread layout is hard-coded, I will make it configurable later.

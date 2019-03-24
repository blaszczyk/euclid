# Euclid
solve problems in Euclidean geometry
### usage
after successful build, execute `euCLId.(bat|sh)` with a problem file as argument
### problem file syntax
* all whitespaces are ignored
* `#` declares the beginning of comments
* a line defines a key value pair separated by `=`
* keys are case insensitive
* keys are variables for numbers, points or curves
  * a number is an arithmetic expression build of
    * parsable floating point numbers
    * basic operations `+`, `-`, `*` and `/`
    * parentheses `(...)`
    * square roots `sqrt(...)`
  * the point with coordinates `x` and `y` is `p(x, y)`
  * the line through the points `x` and `y` is `l(x; y)`
  * the circle with center `c` through the point `x` is `c(c; x)`
* the following mandatory keys define the problem, with list separator `:`
  * `initialPoints` list of points to start with
  * `initialCurves` list of curves to start with
  * `requiredPoints` list of points to be constructed for the solution
  * `requiredCurves` list of curves to be constructed for the solution
  * `maxDepth` maximal depth of the search algorithm

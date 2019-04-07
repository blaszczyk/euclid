# Euclid
solve problems in Euclidean geometry
### usage
after successful build, execute `euCLId.(bat|sh) -file {problemFileName}`
### problem file syntax
* all whitespaces are ignored
* `#` declares the beginning of comments
* a line defines a key value pair separated by `=`
* keys are case insensitive
* keys are variables for numbers, points or curves
  * a number is an arithmetic expression build of
    * parsable floating point numbers
    * constants `pi` and `e` or `rand`
    * basic operations `+`, `-`, `*` and `/`
    * parentheses `(...)`
    * unary functions like `sqrt()`, `sin()`, `exp()` and lots more
    * previously defined numeric variables
  * the point with coordinates `x` and `y` is `p(x, y)`
  * the line through the points `x` and `y` is `l(x; y)`
  * the line segment from point `x` to `y` is `s(x; y)`
  * the circle with center `c` through the point `x` is `c(c; x)`
* the following mandatory keys define the problem, with list separator `:`
  * `initial` list of points and curves to start with
  * `required` list of points and curves to be constructed
  * `algorithm` choice of search algorithm (`curve_based` vs `point_based`)
  * `maxDepth` maximal depth of the search algorithm
  * `findAll` search for all solutions within depth range

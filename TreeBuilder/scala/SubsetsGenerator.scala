// Creates all possible combinations of indices
// in the given range and of the specified length
package modules

object SubsetsGenerator {
  
  def subsets(originalSetSize: Int, maxSubsetSize: Int) =
    (0 to (originalSetSize - 1)).toList combinations maxSubsetSize
    
  def subsetsAsList(originalSetSize: Int, maxSubsetSize: Int) =
    (0 to (originalSetSize - 1) combinations maxSubsetSize).toList
    
  def append[T](xs: List[T], ys: List[T]): List[T] =
    xs match { 
      case List() => ys
      case x :: xs1 => x :: append(xs1, ys)
  }
  
  def remainingSubsetsAsList(newIndex: Int, oldIndices: List[Int], subsetSize: Int): List[List[Int]] = {
	var res = List[List[Int]]()
	var li = (oldIndices combinations (subsetSize - 1)).toList
	for (i <- li) {
		var tmp = append(i, List(10))
		res = append(res, List(tmp))
	}
	return res
  }
  
  def remainingSubsets(newIndex: Int, subsetSize: Int) = {
	var it = ((0 to (newIndex - 1)).toList combinations (subsetSize - 1))
	for (i <- it) yield (newIndex :: i) 
  }
  
}

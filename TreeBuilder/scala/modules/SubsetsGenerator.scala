// Creates all possible combinations of indices
// in the given range and of the specified length
package modules

object SubsetsGenerator {
  
  def subsets(originalSetSize: Int, maxSubsetSize: Int) =
    0 to (originalSetSize - 1) combinations maxSubsetSize
  
}

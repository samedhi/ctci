;; Describe how you would use a single array to implement three stacks

;; Location | Description
;; 1 | Count of the number of stacks in this array (C)
;; C * 3 | Array Metadata for each stack, where [Stack Index, Stack Size, Stack Capacity]
;; Remaining space is used for the respective stacks

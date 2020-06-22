import cv2

# import the necessary packages
import scipy
#from scipy.spatial import distance as dist
#from imutils import perspective
#from imutils import contours
#import imutils
import numpy as np
import contours
import convenience
import perspective
import cv2




def midpoint(ptA, ptB):
	return ((ptA[0] + ptB[0]) * 0.5, (ptA[1] + ptB[1]) * 0.5)
def pyt():
	img = cv2.imread('/data/data/com.example.instantmeasure/app_imageDir/new.png')
	gray = cv2.cvtColor(img, cv2.COLOR_BGR2GRAY)
	gray = cv2.GaussianBlur(gray, (7, 7), 0)
	# perform edge detection, then perform a dilation + erosion to
	# close gaps in between object edges
	edged = cv2.Canny(gray, 50, 100)
	edged = cv2.dilate(edged, None, iterations=1)
	edged = cv2.erode(edged, None, iterations=1)

	# find contours in the edge map
	cnts = cv2.findContours(edged.copy(), cv2.RETR_EXTERNAL,
							cv2.CHAIN_APPROX_SIMPLE)
	cnts = convenience.grab_contours(cnts)

	# sort the contours from left-to-right and initialize the
	# 'pixels per metric' calibration variable
	(cnts, _) = contours.sort_contours(cnts)
	pixelsPerMetric = None
	orig = img.copy()
	# loop over the contours individually
	for c in cnts:
		# if the contour is not sufficiently large, ignore it
		if cv2.contourArea(c) < 100:
			continue

		# compute the rotated bounding box of the contour
		orig = img.copy()
		box = cv2.minAreaRect(c)
		box = cv2.cv.BoxPoints(box) if convenience.is_cv2() else cv2.boxPoints(box)
		box = np.array(box, dtype="int")

		# order the points in the contour such that they appear
		# in top-left, top-right, bottom-right, and bottom-left
		# order, then draw the outline of the rotated bounding
		# box
		box = perspective.order_points(box)
		cv2.drawContours(orig, [box.astype("int")], -1, (0, 255, 0), 2)

		# loop over the original points and draw them
		for (x, y) in box:
			cv2.circle(orig, (int(x), int(y)), 5, (0, 0, 255), -1)

		# unpack the ordered bounding box, then compute the midpoint
		# between the top-left and top-right coordinates, followed by
		# the midpoint between bottom-left and bottom-right coordinates
		(tl, tr, br, bl) = box
		(tltrX, tltrY) = midpoint(tl, tr)
		(blbrX, blbrY) = midpoint(bl, br)

		# compute the midpoint between the top-left and top-right points,
		# followed by the midpoint between the top-righ and bottom-right
		(tlblX, tlblY) = midpoint(tl, bl)
		(trbrX, trbrY) = midpoint(tr, br)

		# draw the midpoints on the image
		cv2.circle(orig, (int(tltrX), int(tltrY)), 5, (255, 0, 0), -1)
		cv2.circle(orig, (int(blbrX), int(blbrY)), 5, (255, 0, 0), -1)
		cv2.circle(orig, (int(tlblX), int(tlblY)), 5, (255, 0, 0), -1)
		cv2.circle(orig, (int(trbrX), int(trbrY)), 5, (255, 0, 0), -1)

		# draw lines between the midpoints
		cv2.line(orig, (int(tltrX), int(tltrY)), (int(blbrX), int(blbrY)),
				 (255, 0, 255), 2)
		cv2.line(orig, (int(tlblX), int(tlblY)), (int(trbrX), int(trbrY)),
				 (255, 0, 255), 2)

		# compute the Euclidean distance between the midpoints
		dA = scipy.spatial.distance.euclidean((tltrX, tltrY), (blbrX, blbrY))
		dB = scipy.spatial.distance.euclidean((tlblX, tlblY), (trbrX, trbrY))

		# if the pixels per metric has not been initialized, then
		# compute it as the ratio of pixels to supplied metric
		# (in this case, inches)
		if pixelsPerMetric is None:
			pixelsPerMetric = dB / 0.955

		# compute the size of the object
		dimA = dA / pixelsPerMetric
		dimB = dB / pixelsPerMetric

		# draw the object sizes on the image
		cv2.putText(orig, "{:.1f}in".format(dimA),
					(int(tltrX - 15), int(tltrY - 10)), cv2.FONT_HERSHEY_SIMPLEX,
					0.65, (255, 255, 255), 2)
		cv2.putText(orig, "{:.1f}in".format(dimB),
					(int(trbrX + 10), int(trbrY)), cv2.FONT_HERSHEY_SIMPLEX,
					0.65, (255, 255, 255), 2)

	cv2.imwrite('/data/data/com.example.instantmeasure/app_imageDir/post.jpg',orig)
	return ""

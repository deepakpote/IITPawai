from django.conf.urls import url, include
from rest_framework import routers
from users import views as userView
from commons import views as commonViews

router = routers.DefaultRouter()
router.register(r'user', userView.UserViewSet)
#router.register(r'district', commonViews.DistrictViewSet)
router.register(r'code', commonViews.CodeViewSet)

# Wire up our API using automatic URL routing.
# Additionally, we include login URLs for the browsable API.
urlpatterns = [
    url(r'^', include(router.urls)),
    #url(r'^api-auth/', include('rest_framework.urls', namespace='rest_framework'))
]
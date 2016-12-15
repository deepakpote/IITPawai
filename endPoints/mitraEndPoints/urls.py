from django.conf.urls import url, include
from rest_framework import routers
from users import views as userView
from commons import views as commonViews
from events import views as eventViews
from contents import views as contentViews
import settings
from django.conf.urls.static import static
from django.contrib.staticfiles.urls import staticfiles_urlpatterns

router = routers.DefaultRouter()
router.register(r'user', userView.UserViewSet)
router.register(r'code', commonViews.CodeViewSet)
router.register(r'events',eventViews.EventViewSet, base_name='events')
router.register(r'content', contentViews.ContentViewSet)
router.register(r'news', commonViews.NewsViewSet)

# Wire up our API using automatic URL routing.
# Additionally, we include login URLs for the browsable API.
urlpatterns = [
    url(r'^', include(router.urls)),
    #url(r'^api-auth/', include('rest_framework.urls', namespace='rest_framework'))
]

urlpatterns += static(settings.STATIC_URL)